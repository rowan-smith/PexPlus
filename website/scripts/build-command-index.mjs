#!/usr/bin/env node
/**
 * Build a searchable command index from docs/commands/*.md (modern-first).
 */
import fs from 'node:fs';
import path from 'node:path';

const ROOT = path.resolve(import.meta.dirname, '..');
const COMMANDS_DIR = path.join(ROOT, 'docs', 'commands');
const OUTPUT = path.join(ROOT, 'src', 'data', 'commands.json');

const CATEGORY_LABELS = {
  general: 'General',
  users: 'Users',
  groups: 'Groups',
  permissions: 'Permissions',
  worlds: 'Worlds',
  ranks: 'Ranks',
};

const FRAMEWORK_ORDER = {modern: 0, both: 1, classic: 2};

/** Commands that are modern-only or best documented outside auto-extraction. */
const MODERN_SUPPLEMENT = [
  {command: '/pex', syntax: '/pex help', summary: 'Show the modern command summary.', category: 'General', docSlug: '/commands/general', anchor: 'pex'},
  {command: '/pex backend', syntax: '/pex backend info', summary: 'Show active backend.', category: 'General', docSlug: '/commands/general', anchor: 'pex-backend'},
  {command: '/pex backend switch', syntax: '/pex backend switch <backend>', summary: 'Switch the active storage backend.', category: 'General', docSlug: '/commands/general', anchor: 'pex-backend'},
  {command: '/pex backend import', syntax: '/pex backend import <backend>', summary: 'Import data from another backend.', category: 'General', docSlug: '/commands/general', anchor: 'pex-backend'},
  {command: '/pex backend export', syntax: '/pex backend export [backend]', summary: 'Export permission data as YAML.', category: 'General', docSlug: '/commands/general', anchor: 'pex-backend'},
  {command: '/pex contexts', syntax: '/pex contexts', summary: 'List active permission contexts.', category: 'General', docSlug: '/commands/general', anchor: 'pex-contexts'},
  {command: '/pex debug', syntax: '/pex debug on', summary: 'Enable permission debug logging.', category: 'General', docSlug: '/commands/general', anchor: 'pex-debug'},
  {command: '/pex debug', syntax: '/pex debug', summary: 'Show debug logging status.', category: 'General', docSlug: '/commands/general', anchor: 'pex-debug'},
  {command: '/pex user', syntax: '/pex user <user> info', summary: 'User summary (prefix, suffix, groups, permissions).', category: 'Users', docSlug: '/commands/users', anchor: 'pex-user-user--info'},
  {command: '/pex user', syntax: '/pex user <user> permissions list [--world <world>]', summary: 'List effective user permissions in a realm.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions add <permission> [--world <world>]', summary: 'Grant a permission directly to a user.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions remove <permission> [--world <world>]', summary: 'Remove a direct user permission.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions check <permission> [--world <world>]', summary: 'Effective permission check (boolean result).', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions trace <permission> [--world <world>]', summary: 'Trace how a permission was resolved.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions timed list [--world <world>]', summary: 'List timed user permissions.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions timed add <permission> <duration> [--world <world>]', summary: 'Grant a timed user permission.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> permissions timed remove <permission> [--world <world>]', summary: 'Remove a timed user permission.', category: 'Users', docSlug: '/commands/users', anchor: 'permissions'},
  {command: '/pex user', syntax: '/pex user <user> groups list [--world <world>]', summary: 'List user group memberships.', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> groups add <group> [--world <world>]', summary: 'Add a user to a group.', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> groups remove <group> [--world <world>]', summary: 'Remove a user from a group.', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> groups set <groups> [--world <world>]', summary: 'Replace group memberships (comma-separated).', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> groups timed list [--world <world>]', summary: 'List timed group memberships.', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> groups timed add <group> <duration> [--world <world>]', summary: 'Timed group membership.', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> groups timed remove <group> [--world <world>]', summary: 'Remove timed group membership.', category: 'Users', docSlug: '/commands/users', anchor: 'groups'},
  {command: '/pex user', syntax: '/pex user <user> options list [--world <world>]', summary: 'List user options and meta.', category: 'Users', docSlug: '/commands/users', anchor: 'options-prefix-suffix-meta'},
  {command: '/pex user', syntax: '/pex user <user> options set <option> <value> [--world <world>]', summary: 'Set prefix, suffix, or custom options.', category: 'Users', docSlug: '/commands/users', anchor: 'options-prefix-suffix-meta'},
  {command: '/pex group', syntax: '/pex group <group> info', summary: 'Group summary (weight, prefix, parents, members).', category: 'Groups', docSlug: '/commands/groups', anchor: 'pex-group-group--info'},
  {command: '/pex group', syntax: '/pex group <group> permissions list [--world <world>]', summary: 'List effective group permissions.', category: 'Groups', docSlug: '/commands/groups', anchor: 'permissions'},
  {command: '/pex group', syntax: '/pex group <group> permissions add <permission> [--world <world>]', summary: 'Grant a permission to a group.', category: 'Groups', docSlug: '/commands/groups', anchor: 'permissions'},
  {command: '/pex group', syntax: '/pex group <group> permissions remove <permission> [--world <world>]', summary: 'Remove a group permission.', category: 'Groups', docSlug: '/commands/groups', anchor: 'permissions'},
  {command: '/pex group', syntax: '/pex group <group> permissions check <permission> [--world <world>]', summary: 'Effective group permission check.', category: 'Groups', docSlug: '/commands/groups', anchor: 'permissions'},
  {command: '/pex group', syntax: '/pex group <group> permissions trace <permission> [--world <world>]', summary: 'Trace group permission resolution.', category: 'Groups', docSlug: '/commands/groups', anchor: 'permissions'},
  {command: '/pex group', syntax: '/pex group <group> permissions timed add <permission> <duration> [--world <world>]', summary: 'Grant a timed group permission.', category: 'Groups', docSlug: '/commands/groups', anchor: 'permissions'},
  {command: '/pex group', syntax: '/pex group <group> members list', summary: 'List group members.', category: 'Groups', docSlug: '/commands/groups', anchor: 'members'},
  {command: '/pex group', syntax: '/pex group <group> members add <user> [--world <world>]', summary: 'Add a member to a group.', category: 'Groups', docSlug: '/commands/groups', anchor: 'members'},
  {command: '/pex group', syntax: '/pex group <group> options set weight <value>', summary: 'Set group weight for prefix priority.', category: 'Groups', docSlug: '/commands/groups', anchor: 'options-weight-prefix-suffix'},
  {command: '/pex groups', syntax: '/pex groups', summary: 'List all groups.', category: 'Groups', docSlug: '/commands/groups', anchor: 'pex-groups-list'},
  {command: '/pex users', syntax: '/pex users', summary: 'List all users.', category: 'Users', docSlug: '/commands/users', anchor: 'pex-users-list'},
  {command: '/pex hierarchy', syntax: '/pex hierarchy [--world <world>]', summary: 'Print permission tree (optional realm flag).', category: 'General', docSlug: '/commands/general', anchor: 'pex-hierarchy'},
  {command: '/pex ladder', syntax: '/pex ladders', summary: 'List rank ladders.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'modern-list-ladders'},
  {command: '/pex ladder', syntax: '/pex ladder <ladder> info', summary: 'Show ladder details.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'modern-list-ladders'},
  {command: '/pex ladder', syntax: '/pex ladder <ladder> promote <user>', summary: 'Promote a player on a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'modern-promote-and-demote'},
  {command: '/pex ladder', syntax: '/pex ladder <ladder> demote <user>', summary: 'Demote a player on a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'modern-promote-and-demote'},
  {command: '/pex ladder', syntax: '/pex ladder <ladder> groups add <group>', summary: 'Add a group to a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'modern-manage-ladder-groups'},
  {command: '/pex ladder', syntax: '/pex ladder <ladder> groups move <group> <rank>', summary: 'Change a group rank on a ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'modern-manage-ladder-groups'},
];

const CLASSIC_SUPPLEMENT = [
  {command: '/promote', syntax: '/promote <user> [ladder]', summary: 'Classic — promote on a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'classic-promote-and-demote'},
  {command: '/demote', syntax: '/demote <user> [ladder]', summary: 'Classic — demote on a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'classic-promote-and-demote'},
  {command: '/pex promote', syntax: '/pex promote <user> [ladder]', summary: 'Classic — promote on a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'classic-promote-and-demote'},
  {command: '/pex demote', syntax: '/pex demote <user> [ladder]', summary: 'Classic — demote on a rank ladder.', category: 'Ranks', docSlug: '/commands/ranks', anchor: 'classic-promote-and-demote'},
  {command: '/pex user', syntax: '/pex user <user> add <permission> [world]', summary: 'Classic — grant a permission to a user.', category: 'Users', docSlug: '/commands/users', anchor: 'pex-user-user-add'},
  {command: '/pex user', syntax: '/pex user <user> check <permission> [world]', summary: 'Classic — check permission (expression detail).', category: 'Users', docSlug: '/commands/users', anchor: 'pex-user-user-check'},
  {command: '/pex user', syntax: '/pex user <user> list [world]', summary: 'Classic — list user permissions.', category: 'Users', docSlug: '/commands/users', anchor: 'pex-user-user-list'},
  {command: '/pex user', syntax: '/pex user <user> group add <group> [world] [lifetime]', summary: 'Classic — add user to group.', category: 'Users', docSlug: '/commands/users', anchor: 'pex-user-user-group-add'},
  {command: '/pex user', syntax: '/pex user <user> timed add <permission> <lifetime> [world]', summary: 'Classic — timed user permission.', category: 'Users', docSlug: '/commands/users', anchor: 'pex-user-user-timed-add'},
  {command: '/pex group', syntax: '/pex group <group> list [world]', summary: 'Classic — list group permissions.', category: 'Groups', docSlug: '/commands/groups', anchor: 'pex-group-group-list'},
  {command: '/pex group', syntax: '/pex group <group> add <permission> [world]', summary: 'Classic — grant permission to group.', category: 'Groups', docSlug: '/commands/groups', anchor: 'pex-group-group-add'},
  {command: '/pex world', syntax: '/pex world <world>', summary: 'Classic — show realm inheritance info.', category: 'Worlds', docSlug: '/commands/worlds', anchor: 'pex-world-world'},
  {command: '/pex world', syntax: '/pex world <world> inherit <parents>', summary: 'Classic — set realm inheritance parents.', category: 'Worlds', docSlug: '/commands/worlds', anchor: 'pex-world-world-inherit'},
  {command: '/pex toggle debug', syntax: '/pex toggle debug', summary: 'Classic — toggle debug logging.', category: 'General', docSlug: '/commands/general', anchor: 'pex-debug'},
];

function slugifyHeading(heading) {
  return heading
    .replace(/`/g, '')
    .toLowerCase()
    .replace(/[^\w\s-]/g, '')
    .trim()
    .replace(/\s+/g, '-');
}

function detectFramework(syntax, command) {
  const s = `${command} ${syntax}`.toLowerCase();

  const modernSignals = [
    '--world',
    '--server',
    ' permissions ',
    ' groups ',
    ' options ',
    ' members ',
    ' parents ',
    ' backend list',
    'backend switch',
    'backend import',
    'backend export',
    'backend info',
    '/pex contexts',
    '/pex debug on',
    '/pex debug off',
    '/pex ladders',
    '/pex ladder',
    ' permissions timed ',
    ' groups timed ',
  ];
  if (modernSignals.some((signal) => s.includes(signal))) {
    return 'modern';
  }

  const classicSignals = [
    '/promote',
    '/demote',
    '/pex promote',
    '/pex demote',
    '/pex config',
    '/pex convert uuid',
    '/pex toggle debug',
    '/pex worlds',
    '/pex world ',
    ' superperms',
    ' swap ',
    ' timed add',
    ' timed remove',
    ' group add ',
    ' group remove ',
    ' group set ',
    ' group list',
    ' prefix ',
    ' suffix ',
    ' weight ',
    '[world]',
    '/pex import',
    '/pex export',
  ];
  if (classicSignals.some((signal) => s.includes(signal))) {
    return 'classic';
  }

  if (command === '/pex reload' || command === '/pex version' || command === '/pex report' || command === '/pex hierarchy') {
    return 'both';
  }

  return 'both';
}

function extractCommandsFromFile(filePath, category, categoryLabel) {
  const content = fs.readFileSync(filePath, 'utf8');
  const body = content.replace(/^---[\s\S]*?---\n/, '');
  const commands = [];
  const seen = new Set();

  const addCommand = (command, syntax, summary, docSlug, frameworkOverride) => {
    const syntaxValue = syntax || command;
    const framework = frameworkOverride ?? detectFramework(syntaxValue, command);
    const key = `${framework}:${docSlug}:${syntaxValue}`;
    if (seen.has(key)) return;
    seen.add(key);
    commands.push({
      command,
      syntax: syntaxValue,
      summary: summary || '',
      category: categoryLabel,
      docSlug,
      anchor: slugifyHeading(command),
      framework,
    });
  };

  const sections = body.split(/\n---\n/);

  for (const section of sections) {
    const headingMatch = section.match(/^## `?(\/pex[^`\n]+)`?/m);
    if (headingMatch) {
      const command = headingMatch[1].trim();
      const syntaxMatch = section.match(/\*\*Syntax:\*\*\s*`([^`]+)`/);
      const summaryMatch = section.match(/\*\*Syntax:\*\*[^\n]*\n\n([^\n`][^\n]*)/);
      addCommand(
        command,
        syntaxMatch?.[1],
        summaryMatch?.[1]?.trim(),
        `/commands/${category}`,
      );
      continue;
    }

    const headingText = section.match(/^## ([^\n]+)/m)?.[1]?.trim();
    const codeMatch = section.match(/```text\n(\/pex[^\n]+)\n```/);
    if (codeMatch && headingText) {
      addCommand(
        codeMatch[1],
        codeMatch[1],
        headingText,
        `/commands/${category}`,
      );
    }
  }

  return commands;
}

const allCommands = [];

for (const entry of MODERN_SUPPLEMENT) {
  allCommands.push({...entry, framework: 'modern'});
}

for (const entry of CLASSIC_SUPPLEMENT) {
  allCommands.push({...entry, framework: 'classic'});
}

for (const [category, label] of Object.entries(CATEGORY_LABELS)) {
  const filePath = path.join(COMMANDS_DIR, `${category}.md`);
  if (!fs.existsSync(filePath)) continue;
  allCommands.push(...extractCommandsFromFile(filePath, category, label));
}

const deduped = [];
const seen = new Set();
for (const entry of allCommands) {
  const key = `${entry.framework}:${entry.docSlug}:${entry.syntax}`;
  if (seen.has(key)) continue;
  seen.add(key);
  deduped.push(entry);
}

deduped.sort((a, b) => {
  const frameworkDelta = (FRAMEWORK_ORDER[a.framework] ?? 9) - (FRAMEWORK_ORDER[b.framework] ?? 9);
  if (frameworkDelta !== 0) return frameworkDelta;
  return a.syntax.localeCompare(b.syntax);
});

fs.mkdirSync(path.dirname(OUTPUT), {recursive: true});
fs.writeFileSync(OUTPUT, `${JSON.stringify(deduped, null, 2)}\n`);
console.log(`Built command index: ${deduped.length} commands → ${OUTPUT}`);
