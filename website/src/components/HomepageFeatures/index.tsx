import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<'svg'>>;
  description: ReactNode;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'High Performance',
    description: (
      <>
        Built from the ground up for speed. PermissionsExPlus handles thousands 
        of players and complex permission trees without breaking a sweat.
      </>
    ),
  },
  {
    title: 'Legacy Compatibility',
    description: (
      <>
        Migrate with confidence. Our legacy adapters provide full binary and 
        command compatibility for existing PEX 1.23.4 integrations.
      </>
    ),
  },
  {
    title: 'Context Aware',
    description: (
      <>
        Advanced context resolution allows for powerful per-world, per-server, 
        and custom state-based permission management.
      </>
    ),
  },
];

function Feature({title, description}: Partial<FeatureItem>) {
  return (
    <div className={clsx('col col--4')}>
      <div className={styles.featureCard}>
        <div className={styles.featureIcon}>
          {title === 'High Performance' && '⚡'}
          {title === 'Legacy Compatibility' && '🔄'}
          {title === 'Context Aware' && '🌍'}
        </div>
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): ReactNode {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
