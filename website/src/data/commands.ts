export type CommandFramework = 'modern' | 'classic' | 'both';

export type CommandEntry = {
  command: string;
  syntax: string;
  summary: string;
  category: string;
  docSlug: string;
  anchor: string;
  framework: CommandFramework;
};
