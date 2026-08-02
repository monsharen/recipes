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
See [RECIPE_FORMAT.md](RECIPE_FORMAT.md) for the full reference. The short version:

## File
- Name it `NameWithoutSpaces_NNNNNN.recipe` — the recipe name with spaces stripped, an underscore, then a 6-digit number.
- Put it in `recipes/`; subfolders (`bread/`, `mains/`, `sauce/`) are scanned recursively.

## metadata
- `name` is **mandatory** — it is the display title.
- `images` is **mandatory** and needs at least one entry; use `- empty.png` if you have no photo. A missing or empty list fails the build.
- `description` follows the convention `From <source url>`.
- `types` are free-form filter tags — reuse existing ones (`Bread, Cookies, Dessert, Drink, Main, Pasta, Pie, Side, Sauce, Marinade, Stew, Sausage, Soup, Noodles, Starter, Pastry`); multiple allowed: `types: [Main,Pie]`.
- `estimatedPrepTime`, `servings` and `notes` are optional free text — put ranges like `10-12 st` in `servings`, never in an ingredient.

## ingredients
- One item per line, always `<quantity> <unit> <description>`.
- Start every line with a number. Use a period for decimals (`0.5`, `2.5`), never a comma, and never a range or a fraction (`2 1/2 dl` → `2.5 dl`).
- The unit must be a known keyword, English or Swedish: `l, dl, cl, ml, g, kg, cups/kopp, tbsp/msk/klick, tsp/tsk, st/u, piece, clove(s)/klyfta/klyftor, can(s)/burk, jar(s), packet(s)/paket, pinch(es)/krm, dash(es)/nypa/nypor, bottle/flaska`. An unknown unit fails the build.
- Exception: with exactly two tokens (`3 lemons`) the second is the description. So `2 large eggs` fails — write `2 st ägg`.
- Unquantified items (salt, pepper, garnishes) still need a number and unit: `1 st salt och peppar`.
- Disambiguate repeated ingredients in the description: `1 st ägg, till degen` / `4 st ägg, till fyllningen`.
- List them in the order they are used.

## actions
- One step per line, always `<action_type> <content>`; the keyword is case-insensitive.
- `free_text` — a normal instruction. Inline HTML is allowed: `Skär köttet i <b>3 cm</b> tärningar.`
- `divider` — a heading grouping the steps under it (`Gör pajdegen`, `Grädda pajen`).
- `image <url>, <caption>` — the URL is everything up to the first comma; the caption is optional.
- `youtube <embed-url>` — use the `/embed/` form.
- Restate quantities inline in each step (`Vispa ihop 4 ägg, 2.5 dl vispgrädde och 1 dl mjölk.`) so a step reads on its own.
- Keep the source recipe's wording and language rather than paraphrasing or translating it.



