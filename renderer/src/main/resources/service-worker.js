/*
 * Service worker for the recipe book.
 *
 * Caching strategy is deliberately conservative. This site has previously shipped a
 * stale-cache bug (Safari serving an outdated recipes.json), so nothing same-origin is
 * ever served from cache while the network is reachable:
 *
 *   same-origin (pages, css, js, recipes.json, dates.json) -> network first, cache as fallback
 *   cross-origin CDN assets (bootstrap, vue)               -> cache first, they are versioned URLs
 *
 * The cache therefore only ever answers when the network does not, which is what makes
 * the site usable offline without risking stale content when online.
 */

const CACHE_VERSION = 'v1';
const CACHE_NAME = 'recept-' + CACHE_VERSION;

// The app shell. Recipe pages are cached as they are visited.
const PRECACHE_URLS = [
  './',
  './index.html',
  './style.css',
  './recipes.js',
  './recipes.json',
  './manifest.webmanifest',
  './icon-192.png',
  './icon-512.png',
  './apple-touch-icon.png',
  './favicon.png',
  'https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css',
  'https://cdnjs.cloudflare.com/ajax/libs/vue/2.0.3/vue.js'
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      // addAll() is atomic: one 404 would fail the whole install, and the CDN entries are
      // outside our control, so each URL is added individually and failures are tolerated.
      .then((cache) => Promise.all(
        PRECACHE_URLS.map((url) => cache.add(url).catch(() => undefined))
      ))
      // Take over straight away rather than waiting for every tab to close.
      .then(() => self.skipWaiting())
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys()
      .then((names) => Promise.all(
        names.filter((name) => name !== CACHE_NAME).map((name) => caches.delete(name))
      ))
      .then(() => self.clients.claim())
  );
});

// Allow the page to trigger an immediate update when a new worker is waiting.
self.addEventListener('message', (event) => {
  if (event.data === 'SKIP_WAITING') {
    self.skipWaiting();
  }
});

// Same-origin responses are never opaque, so a plain 200 check is enough.
function cacheable(response) {
  return response && response.status === 200 && response.type !== 'opaque';
}

// Cross-origin <img> and CDN requests are no-cors, so they come back opaque: status 0 and
// an unreadable body. They still replay correctly from the cache, and recipe photos are
// worth having offline, so they are stored despite success being unverifiable. A failed
// fetch rejects rather than resolving, so offline errors are not cached as images.
function cacheableOpaque(response) {
  return response && (response.ok || response.type === 'opaque');
}

// recipes.js appends a ?t=<timestamp> cache-buster, so cache lookups must ignore the
// query string or the fallback would never match what was stored.
function fromCache(request) {
  return caches.match(request, { ignoreSearch: true });
}

async function networkFirst(request) {
  try {
    const response = await fetch(request);

    if (cacheable(response)) {
      const copy = response.clone();
      const cache = await caches.open(CACHE_NAME);
      // Store without the cache-buster so the next lookup finds it.
      await cache.put(new Request(request.url.split('?')[0], { mode: 'same-origin' }), copy);
    }

    return response;
  } catch (error) {
    const cached = await fromCache(request);

    if (cached) {
      return cached;
    }

    // An uncached page while offline still gets the shell rather than a browser error.
    if (request.mode === 'navigate') {
      const shell = await caches.match('./index.html');
      if (shell) {
        return shell;
      }
    }

    throw error;
  }
}

async function cacheFirst(request) {
  const cached = await fromCache(request);

  if (cached) {
    return cached;
  }

  const response = await fetch(request);

  if (cacheableOpaque(response)) {
    const cache = await caches.open(CACHE_NAME);
    await cache.put(request, response.clone());
  }

  return response;
}

self.addEventListener('fetch', (event) => {
  const request = event.request;

  if (request.method !== 'GET') {
    return;
  }

  const url = new URL(request.url);

  if (url.origin === self.location.origin) {
    event.respondWith(networkFirst(request));
  } else {
    event.respondWith(cacheFirst(request));
  }
});
