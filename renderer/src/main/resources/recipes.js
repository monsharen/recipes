// Remembered card/list preference (defaults to card view).
function readView() {
  try {
    return localStorage.getItem('recipeView') === 'list' ? 'list' : 'card';
  } catch (e) {
    return 'card';
  }
}

// Remembered sort preference (defaults to latest).
function readSort() {
  try {
    const stored = localStorage.getItem('recipeSort');
    return (stored === 'alphabetical' || stored === 'favorites') ? stored : 'latest';
  } catch (e) {
    return 'latest';
  }
}

// Favorited recipes are stored as a list of recipe urls, shared with the
// recipe pages (which write the same key when their star is toggled).
const FAVORITES_KEY = 'recipeFavorites';

function readFavorites() {
  try {
    const stored = JSON.parse(localStorage.getItem(FAVORITES_KEY));
    return Array.isArray(stored) ? stored.filter(url => typeof url === 'string') : [];
  } catch (e) {
    return [];
  }
}

// Remembered "only favorites" filter, like the view and sort preferences. Never
// restored while nothing is starred, so the list can't come back mysteriously empty.
function readFavoritesOnly(favorites) {
  try {
    return favorites.length > 0 && localStorage.getItem('recipeFavoritesOnly') === 'true';
  } catch (e) {
    return false;
  }
}

// GitLab Pages serves everything with "Cache-Control: max-age=600" and custom
// headers can't be set on gitlab.com, so iOS Safari happily shows a stale list
// long after a rebuild. Bypass the HTTP cache for the data files: 'no-store'
// skips the cache entirely, and the cache-buster keeps intermediaries honest.
function fetchFresh(url) {
  return fetch(url + '?t=' + Date.now(), { cache: 'no-store' });
}

// Load recipe data (and optional git-based dates) then start the front-page app.
Promise.all([
  fetchFresh('./recipes.json').then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  }),
  // dates.json is generated at build time (url -> last commit date). Optional.
  fetchFresh('./dates.json').then(response => (response.ok ? response.json() : {})).catch(() => ({}))
])
  .then(([recipes, dates]) => {
    // Recipes get renamed and deleted, which would leave favorites pointing at pages
    // that no longer exist and a count that never matches the list. recipes.json is the
    // source of truth, so drop anything missing from it.
    const known = recipes.map(recipe => recipe.url);
    const stored = readFavorites();
    const favorites = stored.filter(url => known.indexOf(url) !== -1);

    if (favorites.length !== stored.length) {
      try {
        localStorage.setItem(FAVORITES_KEY, JSON.stringify(favorites));
      } catch (e) { /* storage unavailable, ignore */ }
    }

    const app = new Vue({
      el: '#recipeList',
      data: {
        search: '',
        checkedRecipeTypes: [],
        recipes: recipes,
        dates: dates,
        sortBy: readSort(),
        viewMode: readView(),
        favorites: favorites,
        favoritesOnly: readFavoritesOnly(favorites),
      },
      methods: {
        setView(mode) {
          this.viewMode = mode;
          try {
            localStorage.setItem('recipeView', mode);
          } catch (e) { /* storage unavailable, ignore */ }
        },
        setSort(mode) {
          this.sortBy = mode;
          try {
            localStorage.setItem('recipeSort', mode);
          } catch (e) { /* storage unavailable, ignore */ }
        },
        isFavorite(recipe) {
          return this.favorites.indexOf(recipe.url) !== -1;
        },
        toggleFavorite(recipe) {
          const index = this.favorites.indexOf(recipe.url);
          if (index === -1) {
            this.favorites.push(recipe.url);
          } else {
            this.favorites.splice(index, 1);
          }
          try {
            localStorage.setItem(FAVORITES_KEY, JSON.stringify(this.favorites));
          } catch (e) { /* storage unavailable, ignore */ }
        },
        toggleFavoritesOnly() {
          this.favoritesOnly = !this.favoritesOnly;
          try {
            localStorage.setItem('recipeFavoritesOnly', this.favoritesOnly ? 'true' : 'false');
          } catch (e) { /* storage unavailable, ignore */ }
        }
      },
      computed: {
        // The button is an icon, so the count only shows in its tooltip and to
        // screen readers.
        favoritesOnlyLabel() {
          return this.favorites.length
            ? 'Visa endast favoriter (' + this.favorites.length + ')'
            : 'Visa endast favoriter';
        },

        filteredRecipes() {
          const filtered = this.recipes.filter(recipe => {
            return this.checkedRecipeTypes.every(type => {
              let match = recipe?.types?.includes(type);
              return match;
            });
          }).filter(recipe => {
            let searchTerm = this.search.toLowerCase();

            if (searchTerm.length === 0) {
              return true;
            }

            // Check recipe types against search term
            for (let i = 0; i < recipe?.types?.length; i++) {
              let type = recipe.types[i].toLowerCase();
              let match = type.indexOf(searchTerm) > -1;
              if (match) {
                return true;
              }
            }

            // Check recipe name
            return recipe.name.toLowerCase().indexOf(searchTerm) > -1;
          }).filter(recipe => {
            return !this.favoritesOnly || this.isFavorite(recipe);
          });

          const dates = this.dates;
          const sorted = filtered.slice();

          // 'latest' — newest last-commit date first; undated recipes go last.
          const byLatest = (a, b) => {
            const da = dates[a.url] || '';
            const db = dates[b.url] || '';
            if (da === db) {
              return a.name.localeCompare(b.name, 'sv');
            }
            return db.localeCompare(da);
          };

          if (this.sortBy === 'alphabetical') {
            sorted.sort((a, b) => a.name.localeCompare(b.name, 'sv'));
          } else if (this.sortBy === 'favorites') {
            // Favorites float to the top, everything else keeps the latest order.
            sorted.sort((a, b) => {
              const fa = this.isFavorite(a) ? 0 : 1;
              const fb = this.isFavorite(b) ? 0 : 1;
              if (fa !== fb) {
                return fa - fb;
              }
              return byLatest(a, b);
            });
          } else {
            sorted.sort(byLatest);
          }

          return sorted;
        },

        recipeTypes() {
          let types = [];
          this.recipes.forEach(recipe => {
            let recipeTypes = recipe.types;

            recipeTypes?.forEach(type => {
              if (!types.includes(type)) {
                types.push(type);
              }
            });
          });

          return types;
        }
      }
    });

    // Stars can be toggled outside this page: on a recipe page the user came back
    // from (browsers restore the front page from memory rather than reloading it),
    // or in another tab. Re-read the stored list instead of trusting the copy in
    // memory, so the stars are never out of date.
    function syncFavorites() {
      app.favorites = readFavorites().filter(url => known.indexOf(url) !== -1);
    }

    window.addEventListener('pageshow', event => {
      if (event.persisted) {
        syncFavorites();
      }
    });

    window.addEventListener('storage', event => {
      if (event.key === FAVORITES_KEY) {
        syncFavorites();
      }
    });
  })
  .catch(error => {
    console.error('Error fetching recipes:', error);
  });
