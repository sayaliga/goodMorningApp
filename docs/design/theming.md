# Good Morning Design Tokens

This document describes the shared theming resources used throughout the Compose UI. The goal is to keep screens visually consistent and accessible while allowing the product to scale.

## Color palette

| Token | Hex | Usage |
| --- | --- | --- |
| `SkyPrimary` | `#5B8DEF` | Primary buttons, focus rings, navigation highlights |
| `DawnSecondary` | `#FFD166` | Secondary actions, onboarding accents |
| `SoftBackground` | `#F5F7FF` | App background, scaffold base |
| `MidnightSurface` | `#101828` | Dark theme surfaces, high-contrast backgrounds |
| `CalmOnPrimary` | `#FFFFFF` | Text and icons on primary surfaces |
| `TextPrimary` | `#1D2939` | Default body text |
| `TextSecondary` | `#475467` | Supporting copy, metadata |
| `Success` | `#039855` | Confirmation banners, success toasts |
| `Warning` | `#F79009` | Caution states, pending warnings |
| `Error` | `#D92D20` | Error states, destructive actions |

## Typography

| Style | Size / Line height | Weight | Usage |
| --- | --- | --- | --- |
| `displayLarge` | 48 / 56 | Bold | Hero headlines on onboarding |
| `headlineMedium` | 28 / 36 | SemiBold | Section headers, dialog titles |
| `titleMedium` | 18 / 26 | Medium | Card titles, form headings |
| `bodyLarge` | 16 / 24 | Regular | Primary body copy |
| `bodyMedium` | 14 / 20 | Regular | Supporting copy, captions |
| `labelLarge` | 14 / 20 | SemiBold | Buttons, key-value labels |

Use `MaterialTheme.typography` to access these styles. Avoid hard-coded font sizes.

## Spacing scale

```
xxs = 4dp
xs  = 8dp
sm  = 12dp
md  = 16dp
lg  = 24dp
xl  = 32dp
xxl = 40dp
```

Access spacing via `LocalSpacing.current`. This ensures components remain responsive as the scale evolves.

## Elevation and surfaces

- Use `CardDefaults.elevatedCardElevation()` for cards that need emphasis.
- Keep surface backgrounds either `MaterialTheme.colorScheme.surface` (light) or `MidnightSurface` (dark mode) to maintain consistent contrast.

## Iconography

- Prefer outlined icons from the Material Symbols set.
- Maintain a minimum touch target of 48x48dp.

## Accessibility checklist

1. Minimum color contrast ratio of 4.5:1 for text smaller than 18pt (24px).
2. Preserve text scaling by using `sp` units and testing at 200% font size.
3. Announce interactive elements with clear labels; avoid using color alone to convey meaning.
4. Provide keyboard focus indicators; Compose automatically renders focus rings for buttons and text fields.
5. Keep tap targets at least 48dp with generous spacing from surrounding elements.
