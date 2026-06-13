# PermissionsExPlus documentation site

This branch hosts the public documentation for [PermissionsExPlus](https://github.com/rowan-smith/PermissionsExPlus), built with [Docusaurus](https://docusaurus.io/).

## Development

```bash
npm install
npm start
```

Open [http://localhost:3000](http://localhost:3000) to preview changes.

## Production build

```bash
npm run build
npm run serve
```

The static site is written to `build/`.

## Project layout

| Path | Purpose |
|------|---------|
| `docs/` | Documentation pages (Markdown / MDX) |
| `src/` | React components and theme CSS |
| `static/` | Static assets (Javadoc, CNAME, images) |
| `docusaurus.config.ts` | Site configuration |
| `sidebars.ts` | Sidebar navigation |

## Deployment

Pushes to `gh-pages` trigger the GitHub Actions workflow in `.github/workflows/deploy-docs.yml`, which builds and deploys to GitHub Pages.

## Javadoc

Versioned API docs live under `static/apidocs/`. Regenerate with:

```bash
mvn -pl api/permissionsex-api,legacy-api/permissionsex-legacy-api javadoc:javadoc -am -Ddoclint=none
./scripts/build-classic-javadoc.sh STABLE-1.23.4 1.23.4
```
