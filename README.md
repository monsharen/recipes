# Welcome
This is a personal recipe site without the bloat, that promotes careful curation and content improvement over time. 

Live demo: https://monsharen.github.io/recipes (also mirrored at https://monsharen.gitlab.io/recipes)

# What
An interpreter and template engine compiles recipe files into static HTML pages. Ideal content for hosting on gitlab or github pages.

Using pipeline definitions the recipe book is recompiled and deployed on commit. Invalid recipe definitions lead to build failure to support your favorite branching strategy.

# How to get started
Clone the repository to start your own recipe book.

# Build steps
Requires JDK 25 (or later) and Maven 3.9+. Build with `mvn clean install`.

1. Add recipe files in the recipes folder
2. Compile to regenerate the static recipe html pages

# Recipe writing rules
A `.recipe` file is YAML with three top-level keys: `metadata`, `ingredients`, `actions`.
All rules live in [RECIPE_FORMAT.md](RECIPE_FORMAT.md) — read it before adding or editing a recipe.



