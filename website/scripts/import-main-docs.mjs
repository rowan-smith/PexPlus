#!/usr/bin/env node
/**
 * One-time migration helper: import repo-root markdown into Docusaurus docs/.
 * Legacy source files were removed after the gh-pages merge; this script now
 * only refreshes the bootstrap README pointer.
 */
import fs from 'node:fs';
import path from 'node:path';

const WEBSITE_ROOT = path.resolve(import.meta.dirname, '..');
const REPO_ROOT = path.resolve(WEBSITE_ROOT, '..');
const siteVars = JSON.parse(
  fs.readFileSync(path.join(WEBSITE_ROOT, 'site-vars.json'), 'utf8'),
);

const bootstrapReadme = path.join(REPO_ROOT, 'bootstrap/permissionsex-bootstrap/README.md');
fs.writeFileSync(
  bootstrapReadme,
  `# PermissionsExPlus bootstrap (merged jar)

See the [Universal Bootstrap Jar](https://permissionsexplus.rono.dev/developers/bootstrap) documentation on the project website.

Maven module **\`permissionsex-bootstrap\`** (\`dev.rono.permissions:permissionsex-bootstrap\`) emits:

**\`bootstrap/target/PermissionsExPlus-{version}.jar\`**

## Build

From repo root:

\`\`\`bash
mvn -pl bootstrap -am package
\`\`\`

Output: \`bootstrap/target/PermissionsExPlus-${siteVars.version}.jar\`
`,
);

console.log('Updated bootstrap/permissionsex-bootstrap/README.md');
