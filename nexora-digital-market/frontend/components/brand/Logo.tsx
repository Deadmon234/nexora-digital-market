import Image from 'next/image';
import Link from 'next/link';
import { cn } from '@/utils/cn';

const sizes = {
  sm: { height: 36, width: 140 },
  md: { height: 48, width: 180 },
  lg: { height: 72, width: 260 },
};

interface LogoProps {
  size?: keyof typeof sizes;
  className?: string;
  href?: string;
}

export function Logo({ size = 'md', className, href = '/' }: LogoProps) {
  const { height, width } = sizes[size];

  const image = (
    <Image
      src="/images/nexora-logo.png"
      alt="Nexora Digital — Construisons votre avenir numérique"
      height={height}
      width={width}
      className={cn('h-auto w-auto object-contain', className)}
      style={{ maxHeight: height }}
      priority
    />
  );

  if (!href) {
    return image;
  }

  return (
    <Link href={href} className="inline-flex shrink-0 items-center">
      {image}
    </Link>
  );
}
