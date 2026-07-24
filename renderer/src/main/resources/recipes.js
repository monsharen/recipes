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
    return localStorage.getItem('recipeSort') === 'alphabetical' ? 'alphabetical' : 'latest';
  } catch (e) {
    return 'latest';
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
    const app = new Vue({
      el: '#recipeList',
      data: {
        search: '',
        checkedRecipeTypes: [],
        recipes: recipes,
        dates: dates,
        sortBy: readSort(),
        viewMode: readView(),
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
        }
      },
      computed: {
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
          });

          const dates = this.dates;
          const sorted = filtered.slice();

          if (this.sortBy === 'alphabetical') {
            sorted.sort((a, b) => a.name.localeCompare(b.name, 'sv'));
          } else {
            // 'latest' — newest last-commit date first; undated recipes go last.
            sorted.sort((a, b) => {
              const da = dates[a.url] || '';
              const db = dates[b.url] || '';
              if (da === db) {
                return a.name.localeCompare(b.name, 'sv');
              }
              return db.localeCompare(da);
            });
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
  })
  .catch(error => {
    console.error('Error fetching recipes:', error);
  });
