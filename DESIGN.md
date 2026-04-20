# Design System — langchain4j-init

## Product Context
- **What this is:** A developer-facing demo repo and presentation for quickly evaluating LangChain4j in Java.
- **Who it's for:** Java developers, tech leads, platform teams, and solution architects.
- **Space/industry:** Developer tools, AI infrastructure, backend engineering education.
- **Project type:** Technical presentation + docs-heavy showcase.

## Aesthetic Direction
- **Direction:** Neon Blueprint
- **Decoration level:** Intentional
- **Mood:** Technical and trustworthy with a forward-looking energy. Should feel like "production-ready engineering" rather than a startup pitch deck.
- **Personality keywords:** precise, modern, opinionated, fast, practical.

## Typography
- **Display/Hero:** `Space Grotesk` (700) — geometric and bold, strong for headlines.
- **Body:** `Manrope` (500) — highly readable for dense technical bullets.
- **UI/Labels:** `Manrope` (600) — clear for controls and metadata.
- **Data/Tables:** `IBM Plex Mono` (500) — legible code and tabular comparisons.
- **Code:** `IBM Plex Mono` (500)
- **Loading:**
  - `https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;700&family=Manrope:wght@500;600;700&family=IBM+Plex+Mono:wght@400;500&display=swap`
- **Scale:**
  - `display-xl` 64px / 1.05
  - `display-lg` 44px / 1.1
  - `title` 32px / 1.2
  - `h3` 22px / 1.3
  - `body-lg` 22px / 1.55
  - `body` 18px / 1.6
  - `meta` 14px / 1.4

## Color
- **Approach:** Dark-surface with electric accent and high contrast.
- **Primary:** `#00D4FF` — key highlights, links, active states.
- **Secondary:** `#6EF3A5` — positive states and benefit callouts.
- **Accent 3:** `#7AA2FF` — secondary emphasis and diagrams.
- **Neutrals:**
  - `#070B14` background base
  - `#0D1220` elevated surface
  - `#141C2E` card surface
  - `#24304A` border
  - `#B9C7E6` body text
  - `#EAF1FF` high-emphasis text
- **Semantic:**
  - success `#6EF3A5`
  - warning `#FFC56B`
  - error `#FF6E8A`
  - info `#00D4FF`
- **Dark mode:** Native-first dark theme. Light mode is optional for docs pages, not required for slides.

## Spacing
- **Base unit:** 8px
- **Density:** Comfortable
- **Scale:** `2xs 4`, `xs 8`, `sm 12`, `md 16`, `lg 24`, `xl 32`, `2xl 48`, `3xl 64`

## Layout
- **Approach:** Grid-disciplined with centered storytelling.
- **Grid:** 12-column mental model; slide content constrained to readable max width.
- **Max content width:** 1100px visual area; text blocks 760–860px.
- **Border radius:** `sm 6px`, `md 10px`, `lg 16px`, `xl 24px`, `pill 999px`.

## Motion
- **Approach:** Minimal-functional with subtle polish.
- **Easing:**
  - enter `cubic-bezier(0.2, 0.8, 0.2, 1)`
  - exit `cubic-bezier(0.4, 0, 1, 1)`
  - move `cubic-bezier(0.2, 0.7, 0.3, 1)`
- **Duration:**
  - micro `80ms`
  - short `180ms`
  - medium `280ms`
  - long `420ms`

## Component Styling Rules
- **Slides:** Elevated dark cards over atmospheric radial gradients.
- **Headings:** Space Grotesk, tight tracking, strong contrast.
- **Body copy:** Manrope, restrained width, clear rhythm.
- **Code blocks:** Monospace, cool dark panel, subtle border and glow.
- **Tables:** Flat but high-contrast with clear row separation.
- **SVG diagrams:** Use primary/cyan connectors and deep-blue surfaces.
- **Pagination:** Numbered square chips with emoji icon + active cyan fill.

## Content Tone Rules
- Lead with business value before framework details.
- Keep bullets outcome-oriented, no buzzword-only statements.
- Prefer "what to run and what you get" language.
- Label mock/demo behavior clearly (example data, in-memory storage).

## Decisions Log
| Date | Decision | Rationale |
|------|----------|-----------|
| 2026-04-20 | Established "Neon Blueprint" design system | Developer audience needs clarity + technical confidence with visual energy |
| 2026-04-20 | Chose Space Grotesk + Manrope + IBM Plex Mono | Strong hierarchy with excellent code/table legibility |
| 2026-04-20 | Standardized cyan/green accents on dark surfaces | High contrast for projection screens and dense technical content |
