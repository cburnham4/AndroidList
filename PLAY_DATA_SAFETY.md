# Google Play Data Safety Responses

**App:** List (`letshangllc.list`)  
**Play title:** List  
**Version:** 3.0.0 (versionCode 100)  
**Last Updated:** November 2026  
**Target SDK:** 36  
**Play status (prior):** removed by Google

This document provides guidance for the Google Play Console Data Safety form.

---

## Overview

List is a simple offline checklist / shopping-list style app. List items are stored only in local `SharedPreferences` on the device. The app displays AdMob banner ads and uses Google’s User Messaging Platform (UMP) for consent before requesting ads.

There is **no** account system, cloud sync, analytics SDK, or crash reporting SDK in this build.

---

## Data Collection Summary

| Data Type | Collected | Shared | Purpose |
|-----------|-----------|--------|---------|
| List / checklist text | Yes (on-device only) | No | App functionality |
| App interactions | Yes (via AdMob) | Yes (AdMob) | Advertising |
| Advertising ID | Yes (via AdMob) | Yes (AdMob) | Advertising |
| Device / network info | Yes (via AdMob) | Yes (AdMob) | Advertising |
| Approximate location | Possibly (via AdMob) | Yes (AdMob) | Advertising (subject to consent) |

App-authored content (list items) is **not** transmitted off-device by the app code itself.

---

## Detailed Data Types

### 1. App activity / content

#### List items (user-entered text)
- **Collected:** Yes, stored locally in SharedPreferences
- **Shared:** No
- **Purpose:** App functionality
- **Optional:** Yes (user chooses what to enter)
- **Encrypted in transit:** N/A (local only)
- **Deletion:** User can remove items in-app; uninstall clears local data

#### App interactions
- **Collected:** Yes (via Google AdMob / advertising SDKs)
- **Shared:** Yes (Google advertising partners)
- **Purpose:** Advertising
- **User control:** UMP consent dialog (EU/EEA and other regulated regions)

### 2. Device or other IDs

#### Advertising ID
- **Collected:** Yes (via Google AdMob)
- **Shared:** Yes (with Google for advertising)
- **Purpose:** Advertising
- **User control:** Device ad settings + UMP consent where required

---

## Third-Party SDKs

### Google Mobile Ads (AdMob)
- **Data collected:** Advertising ID, app interactions, device info; may include approximate location / diagnostics per Google’s policies
- **Purpose:** Display banner advertisements (HTTPS)
- **Shared:** Yes, with Google advertising partners
- **User control:** UMP consent flow before ads are requested
- **Privacy Policy:** https://policies.google.com/privacy

### Google User Messaging Platform (UMP)
- **Purpose:** GDPR / privacy consent management before ads
- **Data collected:** Consent preferences
- **Privacy Policy:** https://policies.google.com/privacy

Amazon Mobile Ads was **removed** from this modernization (dead / unencrypted ad traffic).

---

## Permissions (Play declarations)

Declared in the manifest:

| Permission | Why |
|------------|-----|
| `INTERNET` | Load AdMob ads over HTTPS; UMP consent network calls |
| `ACCESS_NETWORK_STATE` | Ads SDK checks connectivity before requests |

**Not** declared (and not used by app code):
- Location permissions
- `POST_NOTIFICATIONS`
- Contacts, microphone, camera, storage, etc.

In Play Console, declare AdMob-related data collection/sharing even though the app itself only stores list text locally. Complete any **Advertising ID** / **Ads** declarations required for apps that use AdMob.

---

## Data Security

- **In transit:** Ad and consent traffic uses HTTPS / TLS via Google SDKs
- **At rest:** List items remain in local SharedPreferences only

---

## Children's Privacy / Families

This app is **not** directed at children under 13 (COPPA) or under 16 (GDPR age of consent in relevant regions).

- UMP is configured with `setTagForUnderAgeOfConsent(false)`
- Do **not** enroll this listing in the Designed for Families program while serving personalized AdMob ads unless you reconfigure ads for child-directed treatment and follow Families ads policies

---

## Regional Compliance

### GDPR (EU/EEA)
- UMP consent is requested before ads are loaded when required
- Non-personalized ads may be served when consent is not granted (per Google Ads / UMP behavior)

### CCPA (California)
- Users can limit ad personalization via device-level advertising settings and applicable Google controls

---

## Owner action items in Play Console

1. Fill **Data safety** using this document (ads / advertising ID / app interactions shared with Google).
2. Confirm **Ads** declaration is enabled for the listing.
3. Create real AdMob **App ID** and **banner unit** under publisher `ca-app-pub-8223005482588566`, then replace the TODO placeholders in `app/src/main/res/values/ad_config.xml`.
4. Upload a release signed with your upload key (`keystore.properties` locally; never commit secrets).
5. Review **Sensitive permissions** / Advertising ID policy questionnaires.
6. If the prior removal related to Families or ads policy, ensure the listing audience and ad configuration match current policy before republishing.
