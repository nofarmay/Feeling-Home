// טיפול בתגובות ולייקים
function toggleReplyForm(button) {
    const form = button.closest('.comment')?.querySelector('.reply-form') ||
        button.closest('.post')?.querySelector('.reply-form');
    if (form) {
        form.classList.toggle('active');
    }
}

function toggleLike(button) {
    button.classList.toggle('liked');
    const count = button.querySelector('span');
    const currentLikes = parseInt(count.textContent);
    count.textContent = button.classList.contains('liked') ? currentLikes + 1 : currentLikes - 1;
}

function togglePostLike(button) {
    button.classList.toggle('liked');
    const icon = button.querySelector('.like-icon');
    const count = button.querySelector('.like-count');
    const currentLikes = parseInt(count.textContent);

    if (button.classList.contains('liked')) {
        icon.textContent = '❤️';
        count.textContent = currentLikes + 1;
    } else {
        icon.textContent = '🤍';
        count.textContent = currentLikes - 1;
    }
}

console.log('Feed.js is loaded!');

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM Content Loaded');

    // טיפול בהוספת תגובות חדשות
    document.querySelectorAll('.reply-form .submit-button').forEach(button => {
        button.addEventListener('click', function() {
            const form = this.closest('.reply-form');
            const input = form.querySelector('.comment-input');
            const content = input.value.trim();

            if (content) {
                const commentsSection = this.closest('.post, .comment').querySelector('.nested-comments') ||
                    this.closest('.post').querySelector('.comments-section');

                const newComment = document.createElement('div');
                newComment.className = 'comment';
                newComment.innerHTML = `
                    <div class="comment-header">
                        <img src="/api/placeholder/32/32" alt="תמונת פרופיל" class="comment-avatar">
                        <div class="comment-author">משתמש</div>
                        <time class="comment-time">כרגע</time>
                    </div>
                    <div class="comment-content">${content}</div>
                    <div class="comment-actions">
                        <button onclick="toggleLike(this)" class="action-button">👍 <span>0</span></button>
                        <button onclick="toggleReplyForm(this)" class="action-button">הגב</button>
                    </div>
                    <div class="reply-form">
                        <textarea class="comment-input" rows="2" placeholder="כתוב תגובה..."></textarea>
                        <button class="submit-button">שלח</button>
                    </div>
                    <div class="nested-comments"></div>
                `;

                commentsSection.insertBefore(newComment, commentsSection.firstChild);
                input.value = '';
                form.classList.remove('active');
            }
        });
    });

    // טיפול בסקר
    const radioButtons = document.querySelectorAll('.survey-radio');
    const nextButton = document.querySelector('.btn-next');
    if (nextButton) {
        nextButton.disabled = true;
        radioButtons.forEach(radio => {
            radio.addEventListener('change', function() {
                nextButton.disabled = false;
                nextButton.style.opacity = '1';
            });
        });
    }

    // בדיקה האם מובייל
    const isMobile = window.matchMedia("(max-width: 390px)").matches;
    const drawer = document.querySelector('.contacts-panel');
    const handle = document.querySelector('.drawer-handle');
    const toggleBtn = document.querySelector('.toggle-contacts-btn');

    console.log('Is Mobile?', isMobile);
    if (isMobile) {
        const storiesContainer = document.querySelector('.stories-container');
        if (storiesContainer) {
            storiesContainer.style.display = 'flex';
            storiesContainer.style.flexDirection = 'column';

            const storyItems = document.querySelectorAll('.story-item');
            storyItems.forEach(item => {
                item.style.display = 'flex';
                item.style.flexDirection = 'row';
            });
        }
    }

    // יצירת אוברליי
    const overlay = document.createElement('div');
    overlay.className = 'drawer-overlay';
    document.body.appendChild(overlay);

    if (isMobile) {
        console.log('Mobile mode activated');

        // טיפול בכפתור המובייל
        toggleBtn?.addEventListener('click', () => {
            console.log('Mobile button clicked');
            drawer.classList.toggle('open');

            if (drawer.classList.contains('open')) {
                overlay.classList.add('visible');
            } else {
                overlay.classList.remove('visible');
            }
        });

        // טיפול בגרירה במובייל
        let startY = 0;
        let currentY = 0;
        let isDragging = false;
        let initialTransform = 0;

        function getTransformValue(element) {
            const transform = window.getComputedStyle(element).transform;
            if (transform === 'none') return 0;
            const matrix = new DOMMatrix(transform);
            return matrix.m42;
        }

        function handleTouchStart(e) {
            if (!handle) return;
            isDragging = true;
            startY = e.touches[0].clientY;
            initialTransform = getTransformValue(drawer);
            drawer.style.transition = 'none';
            drawer.style.willChange = 'transform';
        }

        function handleTouchMove(e) {
            if (!isDragging) return;
            e.preventDefault();

            currentY = e.touches[0].clientY;
            const deltaY = currentY - startY;
            const newTransform = Math.max(0, Math.min(deltaY + initialTransform, window.innerHeight - 25));

            drawer.style.transform = `translateY(${newTransform}px)`;

            const progress = 1 - (newTransform / (window.innerHeight - 25));
            overlay.style.opacity = Math.max(0, Math.min(progress, 1));
        }

        function handleTouchEnd() {
            if (!isDragging) return;
            isDragging = false;

            drawer.style.transition = 'transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
            drawer.style.willChange = 'auto';

            const currentTransform = getTransformValue(drawer);
            const threshold = window.innerHeight * 0.3;

            if (currentTransform > threshold) {
                drawer.classList.remove('open');
                overlay.classList.remove('visible');
            } else {
                drawer.classList.add('open');
                overlay.classList.add('visible');
            }
        }

        // הוספת מאזיני אירועים למובייל
        handle?.addEventListener('touchstart', handleTouchStart);
        document.addEventListener('touchmove', handleTouchMove, { passive: false });
        document.addEventListener('touchend', handleTouchEnd);

    } else {
        console.log('Desktop mode activated');

        // הסתרת אלמנטים של המובייל
        if (toggleBtn) toggleBtn.style.display = 'none';
        if (handle) handle.style.display = 'none';
        overlay.style.display = 'none';

        // טיפול בפאנל הדסקטופ
        const desktopToggleBtn = document.querySelector('.toggle-contacts');
        if (desktopToggleBtn) {
            console.log('Desktop toggle button found');
            desktopToggleBtn.addEventListener('click', () => {
                console.log('Desktop button clicked');
                drawer.classList.toggle('collapsed');
                desktopToggleBtn.textContent = drawer.classList.contains('collapsed') ? 'פתח' : 'סגור';
            });
        }
    }

    // טיפול באוברליי (משותף)
    overlay.addEventListener('click', () => {
        drawer.classList.remove('open');
        overlay.classList.remove('visible');
    });
});