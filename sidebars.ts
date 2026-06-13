import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

const sidebars: SidebarsConfig = {
  docsSidebar: [
    {
      type: 'category',
      label: 'Introduction',
      collapsed: false,
      items: [
        'index',
        'requirements',
        'storage',
        'configuration',
      ],
    },
    {
      type: 'category',
      label: 'Concepts',
      collapsed: false,
      items: [
        'concepts/permissions',
        'concepts/inheritance',
        'concepts/context',
        'concepts/weight',
        'concepts/meta',
      ],
    },
    {
      type: 'category',
      label: 'Commands',
      collapsed: false,
      items: [
        'commands/general',
        'commands/users',
        'commands/groups',
        'commands/permissions',
        'commands/worlds',
        'commands/ranks',
      ],
    },
    {
      type: 'category',
      label: 'Guides',
      collapsed: false,
      items: ['guides/recipes', 'guides/troubleshooting'],
    },
    {
      type: 'category',
      label: 'FAQ',
      collapsed: false,
      items: ['faq/default-groups', 'faq/migration'],
    },
    {
      type: 'category',
      label: 'Developers',
      collapsed: false,
      items: [
        'developers/index',
        'developers/cookbook',
        'developers/contributing',
        'developers/reference',
      ],
    },
    {
      type: 'category',
      label: 'Project',
      collapsed: false,
      items: ['about'],
    },
  ],
};

export default sidebars;
