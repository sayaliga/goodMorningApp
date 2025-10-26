# Figma mockups overview

The Good Morning experience is documented in Figma as a single page with four flows. Use the components below to recreate or validate the mockups.

> **Note:** The actual `.fig` source should live in the shared Figma team project. This document captures the structure so engineers can understand the layout when they do not have Figma access.

## Frame naming

1. `GM-Onboarding`
2. `GM-Dashboard`
3. `GM-TemplateGallery`
4. `GM-ShareSheet`

All frames use a 360x800 artboard to align with common Android viewport sizes.

## Onboarding frame

- Hero illustration occupying the top 40% of the frame; use `SkyPrimary` gradient background.
- Headline: "Start your day with personalized greetings" (`displayLarge`).
- Supporting text: 2-line body copy describing automation benefits (`bodyLarge`).
- Primary button: "Get Started", full-width, 16dp corner radius.
- Secondary text button: "Learn more", aligned beneath the primary CTA for marketing experiments.

## Generator dashboard

- App bar containing greeting, e.g. "Morning, Maya" and overflow icon.
- KPI cards row:
  - "Scheduled Sends" (72) with success pill.
  - "Open Rate" (64%) with subtle trend sparkline.
  - "New Templates" (5) with accent badge.
- Campaign list: cards with title, status pill (Scheduled, Draft, Live), and next send timestamp.
- Floating action button bottom right labelled "New Campaign".

## Template gallery

- Search field pinned to top of scroll.
- Tab chips: "All", "Favorites", "Birthdays", "Announcements".
- 2-column grid of template cards (160dp height) showing template name, tone tags, and "Use template" secondary button.
- Sticky filter button bottom-left for quickly accessing filters on mobile.

## Share sheet

- Modal sheet over translucent scrim with 24dp rounded top corners.
- Title: "Share campaign preview".
- Description text (`bodyMedium`): "Send a preview to collaborators before scheduling".
- Input fields: Email (with helper text for comma-separated entries), optional note field.
- Toggle: "Allow edits" (default ON).
- Primary button: "Send preview" (disabled until email provided).
- Secondary text button: "Copy link" aligned right of the primary button.

## Component references

- Buttons leverage Material 3 elevated and tonal variants.
- Chips use AssistChip with icons for quick scanning.
- Status pills use `Success`, `Warning`, or `Error` tokens depending on campaign state.

## Export guidance

When exporting assets:
- Use 2x PNG for marketing illustrations.
- Export vector icons as SVG to keep them crisp.
- Document the prototype link in the product wiki for future updates.
