// bottom-nav.js
function injectBottomNav() {
    if (document.querySelector('.bottom-nav')) return;

    const nav = document.createElement('nav');
    nav.className = 'bottom-nav';
    nav.dir = 'ltr';

    const currentPath = window.location.pathname;
    const currentPage = currentPath.split('/').pop() || 'index.html';

    nav.innerHTML = `
        <a href="/" class="nav-item ${currentPage === '' || currentPage === 'index.html' ? 'active' : ''}">
            <i class="fas fa-home"></i>
        </a>
        <a href="feed.html" class="nav-item ${currentPage === 'feed.html' ? 'active' : ''}">
            <i class="fas fa-stream"></i>
        </a>
           <a href="contacts.html" class="nav-item ${currentPage === 'contacts.html' ? 'active' : ''}">
            <i class="fas fa-address-book"></i>
        </a>
        <a href="messges.html" class="nav-item ${currentPage === 'messges.html' ? 'active' : ''}">
            <i class="fas fa-envelope"></i>
        </a>
        <a href="profile.html" class="nav-item ${currentPage === 'profile.html' ? 'active' : ''}">
            <i class="fas fa-user"></i>
        </a>
    `;

    document.body.appendChild(nav);
}

document.addEventListener('DOMContentLoaded', injectBottomNav);