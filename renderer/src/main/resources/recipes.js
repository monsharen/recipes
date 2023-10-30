import recipes from './recipes.json' assert { type: "json" };

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

                if (this.checkedRecipeTypes.length === 0) {
                    return true;
                }

                // Check recipe types against checkboxes
                for (let i = 0; i < recipe?.types?.length; i++) {
                    let recipeType = recipe.types[i];
                    let match = this.checkedRecipeTypes.includes(recipeType);
                    //console.log("checkedRecipeTypes (" + this.checkedRecipeTypes + ") contains '" + recipeType + "': " + match);
                    if (match) {
                        return true;
                    }
                }

                return false;

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
            })
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

