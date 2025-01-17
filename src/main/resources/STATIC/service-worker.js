const CACHE_NAME = 'feeling-home-v1';
const urlsToCache = [
    '/',
    // HTML files
    '/index.html',
    '/login.html',
    '/profile.html',
    '/feed.html',
    '/messges.html',
    '/contacts.html',

    // CSS files
    '/styles/calendar.css',
    '/styles/contacts.css',
    '/styles/feed.css',
    '/styles/login.css',
    '/styles/main.css',
    '/styles/messages.css',
    '/styles/profile.css',

    // JavaScript files
    '/scripts/bottom-nav.js',
    '/scripts/calendar.js',
    '/scripts/contacts.js',
    '/scripts/feed.js',
    '/scripts/messages.js',
    '/scripts/profile.js',

    // Images
    '/img/3.png',  // הלוגו שלך
    '/icons/icon-72x72.png',
    '/icons/icon-96x96.png',
    '/icons/icon-128x128.png',
    '/icons/icon-144x144.png',
    '/icons/icon-152x152.png',
    '/icons/icon-192x192.png',
    '/icons/icon-384x384.png',
    '/icons/icon-512x512.png',

    // External resources (רק אם הם חשובים במצב offline)
    'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css',
    'https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@200;300;400;500;600;700;800&display=swap'
];

// התקנת Service Worker
self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll(urlsToCache))
    );
});

// הפעלת Service Worker
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(cacheNames => {
            return Promise.all(
                cacheNames
                    .filter(cacheName => cacheName !== CACHE_NAME)
                    .map(cacheName => caches.delete(cacheName))
            );
        })
    );
});

// טיפול בבקשות רשת
self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request)
            .then(response => {
                if (response) {
                    return response; // החזרה מהמטמון אם קיים
                }
                return fetch(event.request); // אם לא קיים במטמון, בקש מהשרת
            })
    );
});