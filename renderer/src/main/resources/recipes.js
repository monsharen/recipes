// Fetch the JSON file and initialize the Vue app once the data is loaded
fetch('./recipes.json')
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    const recipes = data;

    const app = new Vue({
      el: '#recipeList',
      data: {
        search: '',
        checkedRecipeTypes: [],
        recipes: recipes,
      },
      computed: {
        filteredRecipes() {
          return this.recipes.filter(recipe => {
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
