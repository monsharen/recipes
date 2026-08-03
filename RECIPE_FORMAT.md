# Recipe file format

Reference for translating real-world recipes into this project's `.recipe` format.
Files live in `recipes/` (subfolders like `bread/`, `mains/`, `sauce/` are allowed and
scanned recursively). A `.recipe` file is **YAML** with three top-level keys:
`metadata`, `ingredients`, `actions`.

Parsing is done in `parser/` (see `Interpreter.java`, `IngredientParser.java`,
`ActionParser.java`). Unknown YAML properties are ignored; **an invalid file fails the
build**, so structure matters.

## File naming

`NameWithoutSpaces_NNNNNN.recipe` — the recipe name with spaces stripped, an underscore,
then a 6-digit number (effectively random/unique). Example: `Chocolatechipcookies_721358.recipe`.

## metadata

```yaml
metadata:
  name: Chocolate chip cookies        # MANDATORY — display title
  images:                             # MANDATORY — needs at least one entry
    - https://example.com/photo.jpg
  description: From https://source... # free text; convention is "From <source url>"
  notes: "Optional extra tip."        # optional; quote if it contains ':' or special chars
  types: [Cookies]                    # category tags, comma-separated inside []
  estimatedPrepTime: 30 min           # free-form string (e.g. "2 h", "1 tim 30 min")
  servings: 20 st                     # free-form string (e.g. "6", "1 glass", "10-12 st")
```

### Mandatory fields (the build FAILS without them)

- **`name`** — identifies the recipe; without it the front-page card and page have no title.
- **`images`** — must contain **at least one entry**. The front-page renderer calls
  `images.get(0)` unconditionally (`Main.map()`), so a missing `images:` key or an empty list
  throws a `NullPointerException` and fails the pipeline. If you have no photo yet, use a
  placeholder: `- empty.png` (a bundled fallback image). An empty list item (`- ` with no
  value) counts as one entry but renders as a broken card — prefer `empty.png`.

Everything else is optional. Other supported but rarely-used field: `language`. Every value is
stored as-is (strings), so `estimatedPrepTime` and `servings` accept any human text.

**types** are free-form tags used for filtering on the front page. Observed values:
`Bread, Cookies, Dessert, Drink, Main, Pasta, Pie, Side, Sauce, Marinade, Stew, Sausage,
Soup, Noodles, Starter, Pastry`. Reuse existing tags where possible; multiple allowed:
`types: [Main, Stew]`.

## ingredients

A YAML list. **Each line must be `<quantity> <unit> <description>`** and must start with a
number (parsed as a `double` — use a period for decimals, e.g. `0.5`, never a comma; ranges
like `10-12` are NOT valid here — put those in `servings` instead).

```yaml
ingredients:
 - 200 g smör, rumsvarmt
 - 1.5 dl strösocker
 - 4 dl vetemjöl
 - 0.5 tsk salt
 - 1 st ägg          # "st" / "u" = a count of whole items
```

Parsing rule (`IngredientParser`): split on spaces.
- `parts[0]` → quantity (double).
- If there are exactly **two** tokens (`3 lemons`), the unit becomes QUANTITY and the second
  token is the description.
- Otherwise `parts[1]` is looked up as a **unit** (must be a known keyword — see below) and
  the rest of the line is the description.

Because `parts[1]` must resolve to a known unit when there are 3+ tokens, an ingredient like
`2 large eggs` would try to parse `large` as a unit and fail. Use a real unit (`2 st ägg`,
`2 u eggs`) or keep it to two tokens.

### Supported units (keyword → meaning)

Volume/weight: `l` litre, `dl` decilitre, `cl` centilitre, `ml` millilitre,
`g` gram, `kg` kilogram, `cups`/`kopp` cups.
Spoons: `tbsp`/`msk`/`klick` tablespoon, `tsp`/`tsk` teaspoon.
Counts & misc: `st`/`u` quantity (whole items), `piece`, `clove`/`cloves`/`klyfta`/`klyftor`,
`can`/`cans`/`burk`, `jar`/`jars`, `packet`/`packets`/`paket`, `pinch`/`pinches`/`krm`,
`dash`/`dashes`/`nypa`/`nypor`, `bottle`/`flaska`.

Both English and Swedish keywords work. Unknown units fail the build.

### Writing rules

- Never use a fraction — `2 1/2 dl` → `2.5 dl`.
- Unquantified items (salt, pepper, garnishes) still need a number and unit:
  `1 st salt och peppar`.
- Disambiguate repeated ingredients in the description: `1 st ägg, till degen` /
  `4 st ägg, till fyllningen`.
- List ingredients in the order they are used.

## actions

A YAML list of steps. Each line is `<action_type> <content>`. Four action types:

```yaml
actions:
 - divider Make the dough                     # section heading / group label
 - free_text Mix all ingredients. Do not knead.  # a normal instruction step
 - image https://example.com/step.jpg, Optional caption   # inline image (caption after comma)
 - youtube https://www.youtube.com/embed/VIDEO_ID          # embedded video
```

- **free_text** — the workhorse; a plain instruction. Inline HTML is allowed and rendered,
  e.g. `Skär köttet i <b>3 cm</b> tärningar.`
- **divider** — a heading that visually groups the steps under it (e.g. "On the evening" /
  "Bake the bread").
- **image** — `image <url>` optionally followed by `, <caption>`. The URL is everything up to
  the first comma.
- **youtube** — `youtube <embed-url>`. Use the `/embed/` form of the YouTube URL.

The action keyword is case-insensitive and matched by the text before the first space.

### Writing rules

- Keep each step to a single instruction — split multi-action paragraphs into separate
  `free_text` lines.
- Restate quantities inline in each step (`Vispa ihop 4 ägg, 2.5 dl vispgrädde och 1 dl
  mjölk.`) so a step reads on its own.
- Keep the source recipe's wording and language rather than paraphrasing or translating it.

## Minimal template

```yaml
metadata:
  name: Recipe Name
  images:
    - https://example.com/photo.jpg
  description: From https://original-source
  types: [Main]
  estimatedPrepTime: 30 min
  servings: 4

ingredients:
 - 500 g flour
 - 1 tsp salt
 - 3 dl water

actions:
 - divider Prep
 - free_text Do the first thing.
 - free_text Do the next thing.
```
