// Mock data based on Postman response
const contacts = [
    {
        id: 26,
        name: 'Hofit Brahami',
        status: 'offline',
        avatar: '/img/hofit.png',
        phone: '0502345678',
        email: 'hofit.brahami@amdocs.com',
        lastSeen: 'לפני שעה',
        role: 'חברת קהילה'
    },
    {
        id: 29,
        name: 'Gal Cohen',
        status: 'online',
        avatar: '/img/gal.jpeg',
        phone: '0546332722',
        email: 'gal.cohen@grunitech.com',
        lastSeen: 'מחובר/ת',
        role: 'חברת קהילה'
    },
    {
        id: 30,
        name: 'Nissim Brami',
        status: 'online',
        avatar: '/img/nisole.png',
        phone: '0505458400',
        email: 'nissim.brami@grunitech.com',
        lastSeen: 'מחובר/ת',
        role: 'חבר קהילה'
    },
    {
        id: 6,
        name: 'Einav balestra',
        status: 'online',
        avatar: '/img/einav.png',
        phone: 'einav.balestra',
        email: 'einav.balestra@grunitech.com',
        lastSeen: 'מחובר/ת',
        role: 'חברת קהילה'
    },
    {
        id: 27,
        name: 'Elad Klimi',
        status: 'offline',
        avatar: '/img/img.png',
        phone: '0502345678',
        email: 'eladk@sederot.muni.il',
        lastSeen: 'לפני שעתיים',
        role: 'חבר קהילה'
    },
    {
        id: 28,
        name: 'Sarah Fahima',
        status: 'offline',
        avatar: '/img/sara.jpeg',
        phone: '054841172',
        email: 'margishim@sderot.matnasim.co.il',
        lastSeen: 'לפני שעתיים',
        role: 'חברת קהילה'
    },
    {
        id: 19,
        name: 'Uria Kohn',
        status: 'online',
        avatar: '/img/URIYA.png',
        phone: '0508465006',
        email: 'uria.kohn@grunitech.com',
        lastSeen: 'מחובר/ת',
        role: 'חבר קהילה'
    },
];

// יצירת פריט איש קשר
function createContactItem(contact) {
    return `
        <div class="contact-item" data-id="${contact.id}">
            <div class="contact-info">
                <div class="contact-name">${contact.name}</div>
                <div class="contact-role">חבר/ת קהילה</div>
            </div>
            <div class="last-seen">${contact.status === 'online' ? 'מחובר/ת' : 'לפני שעה'}</div>
            <div class="avatar-container">
                <img src="${contact.avatar}" alt="${contact.name}" class="contact-avatar">
                ${contact.status === 'online' ? '<span class="status-indicator online"></span>' : ''}
            </div>
        </div>
    `;
}

// עדכון פרטי איש קשר במודל
function updateContactDetailsModal(contact) {
    const modal = document.getElementById('contactModal');

    // עדכון תמונה ופרטים בסיסיים
    modal.querySelector('.contact-avatar').src = contact.avatar;
    modal.querySelector('.contact-name').textContent = contact.name;
    modal.querySelector('.contact-role').textContent = contact.role;

    // עדכון פרטי קשר
    modal.querySelector('.phone span').textContent = contact.phone;
    modal.querySelector('.email span').textContent = contact.email;

    // סימון אם מועדף
    const starButton = modal.querySelector('.star-button i');
    starButton.className = contact.isFavorite ? 'fas fa-star' : 'far fa-star';
}

// פתיחת מודל פרטי איש קשר
function showContactDetails(contact) {
    updateContactDetailsModal(contact);
    document.getElementById('contactModal').classList.add('active');
}

// סגירת מודל
function closeContactDetails() {
    document.getElementById('contactModal').classList.remove('active');
}

// חיפוש אנשי קשר
function setupSearch() {
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filteredContacts = contacts.filter(contact =>
            contact.name.toLowerCase().includes(searchTerm) ||
            contact.phone.includes(searchTerm) ||
            contact.email?.toLowerCase().includes(searchTerm) ||
            contact.role?.toLowerCase().includes(searchTerm)
        );

        const contactsList = document.getElementById('contactsList');
        contactsList.innerHTML = filteredContacts.map(createContactItem).join('');

        // הוספת מאזיני לחיצה מחדש
        addContactListeners();
    });
}

// הוספת מאזיני לחיצה לאנשי קשר
function addContactListeners() {
    document.querySelectorAll('.contact-item').forEach(item => {
        item.addEventListener('click', () => {
            const contactId = parseInt(item.dataset.id);
            const contact = contacts.find(c => c.id === contactId);
            if (contact) showContactDetails(contact);
        });
    });
}

// טיפול בשיתוף איש קשר
function handleShare(contact) {
    if (navigator.share) {
        navigator.share({
            title: `פרטי קשר - ${contact.name}`,
            text: `${contact.name}\nטלפון: ${contact.phone}\nאימייל: ${contact.email}`,
        }).catch(err => console.error('Error sharing:', err));
    }
}

// טיפול במועדפים
function toggleFavorite(contactId) {
    const contact = contacts.find(c => c.id === contactId);
    if (contact) {
        contact.isFavorite = !contact.isFavorite;
        const starButton = document.querySelector('.star-button i');
        starButton.className = contact.isFavorite ? 'fas fa-star' : 'far fa-star';
    }
}

// אתחול הדף
document.addEventListener('DOMContentLoaded', () => {
    // טעינה ראשונית של אנשי הקשר
    const contactsList = document.getElementById('contactsList');
    contactsList.innerHTML = contacts.map(createContactItem).join('');

    // הגדרת מאזיני אירועים
    addContactListeners();
    setupSearch();

    // הגדרת מאזיני אירועים לכפתורים במודל
    document.querySelector('.star-button').addEventListener('click', (e) => {
        const modal = document.getElementById('contactModal');
        const activeContactId = parseInt(modal.querySelector('.contact-item')?.dataset.id);
        if (activeContactId) toggleFavorite(activeContactId);
    });

    document.querySelector('.share-contact').addEventListener('click', (e) => {
        const modal = document.getElementById('contactModal');
        const activeContactId = parseInt(modal.querySelector('.contact-item')?.dataset.id);
        const contact = contacts.find(c => c.id === activeContactId);
        if (contact) handleShare(contact);
    });
});