/*
 * Registers the service worker. Loaded by both index.html and every recipe page, so the
 * app works offline whichever page the user happened to install or open first.
 */
(function () {
  if (!('serviceWorker' in navigator)) {
    return;
  }

  window.addEventListener('load', function () {
    // Relative path so the worker registers under the site's sub-path
    // (/recipes/ on GitHub and GitLab Pages) rather than the domain root.
    navigator.serviceWorker.register('service-worker.js').then(function (registration) {
      // If an updated worker is waiting, activate it immediately instead of leaving the
      // user on a previous version until every tab is closed.
      function promote(worker) {
        if (worker && worker.state === 'installed' && navigator.serviceWorker.controller) {
          worker.postMessage('SKIP_WAITING');
        }
      }

      promote(registration.waiting);

      registration.addEventListener('updatefound', function () {
        var installing = registration.installing;

        if (!installing) {
          return;
        }

        installing.addEventListener('statechange', function () {
          promote(installing);
        });
      });
    }).catch(function (error) {
      console.error('Service worker registration failed:', error);
    });
  });

  // Reload once when a new worker takes control, so the page matches the new cache.
  var refreshing = false;
  navigator.serviceWorker.addEventListener('controllerchange', function () {
    if (refreshing) {
      return;
    }
    refreshing = true;
    window.location.reload();
  });

  // iOS resumes an installed PWA from memory rather than reloading it, so the front
  // page can show a days-old recipe list even though every fetch is network-first.
  // Reload the front page when the app returns to the foreground after a while;
  // recipe pages are left alone so scroll position survives switching apps mid-cook.
  var STALE_AFTER_MS = 10 * 60 * 1000;
  var hiddenAt = null;

  function isFrontPage() {
    var path = window.location.pathname;
    return path.endsWith('/') || path.endsWith('/index.html');
  }

  document.addEventListener('visibilitychange', function () {
    if (document.visibilityState === 'hidden') {
      hiddenAt = Date.now();
    } else if (isFrontPage() && hiddenAt && Date.now() - hiddenAt > STALE_AFTER_MS) {
      window.location.reload();
    }
  });
})();
