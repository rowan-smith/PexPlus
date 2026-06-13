import Link from '@docusaurus/Link';
import styles from './VersionCards.module.css';

const VERSION = '1.23.5';
const REPO = 'rowan-smith/PermissionsExPlus';

type Card = {
  href: string;
  label: string;
  description: string;
  external?: boolean;
};

const HOME_CARDS: Card[] = [
  {
    href: `https://github.com/${REPO}/releases`,
    label: `Download v${VERSION}`,
    description: 'Get the latest release jar',
    external: true,
  },
  {
    href: '/guides/recipes',
    label: 'Common Setups',
    description: 'Staff ranks, VIP, survival — copy-paste recipes',
  },
  {
    href: '/commands/general',
    label: 'Command Reference',
    description: 'Full /pex command documentation',
  },
];

const JAVADOC_VERSIONS: Card[] = [
  {
    label: '1.23.5 (current)',
    href: 'pathname:///apidocs/1.23.5/index.html',
    description: 'Modern + legacy API (PermissionsExPlus)',
  },
  {
    label: '1.23.4',
    href: 'pathname:///apidocs/1.23.4/index.html',
    description: 'Classic PermissionsEx API',
  },
  {
    label: '1.23.3',
    href: 'pathname:///apidocs/1.23.3/index.html',
    description: 'Classic PermissionsEx API',
  },
  {
    label: '1.23.2',
    href: 'pathname:///apidocs/1.23.2/index.html',
    description: 'Classic PermissionsEx API',
  },
  {
    label: '1.23.1',
    href: 'pathname:///apidocs/1.23.1/index.html',
    description: 'Classic PermissionsEx API',
  },
  {
    label: '1.22.1',
    href: 'pathname:///apidocs/1.22.1/apidocs/index.html',
    description: 'Original PermissionsEx API',
  },
];

type Props = {
  type: 'home' | 'javadoc';
};

export default function VersionCards({type}: Props): JSX.Element {
  const cards = type === 'home' ? HOME_CARDS : JAVADOC_VERSIONS;

  return (
    <div className={styles.grid}>
      {cards.map((card) =>
        card.external ? (
          <a
            key={card.href}
            className={styles.card}
            href={card.href}
            target="_blank"
            rel="noopener noreferrer">
            <span className={styles.label}>{card.label}</span>
            <span className={styles.description}>{card.description}</span>
          </a>
        ) : (
          <Link key={card.href} className={styles.card} to={card.href}>
            <span className={styles.label}>{card.label}</span>
            <span className={styles.description}>{card.description}</span>
          </Link>
        ),
      )}
    </div>
  );
}
