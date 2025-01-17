document.getElementById('change-photo').addEventListener('click', () => {
    document.getElementById('photo-input').click();
});

document.getElementById('photo-input').addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
        // בדיקות בסיסיות
        if (!file.type.startsWith('image/')) {
            alert('נא להעלות קובץ תמונה בלבד');
            e.target.value = '';
            return;
        }

        if (file.size > 5 * 1024 * 1024) {  // 5MB
            alert('התמונה גדולה מדי. הגודל המקסימלי הוא 5MB');
            e.target.value = '';
            return;
        }

        // הצגת התמונה הנבחרת
        const reader = new FileReader();
        reader.onload = (e) => {
            document.getElementById('profile-preview').src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
});

document.getElementById('delete-photo').addEventListener('click', () => {
    // בינתיים רק מוחק את התמונה מהתצוגה
    document.getElementById('profile-preview').src = '/api/placeholder/160/160';
    document.getElementById('photo-input').value = '';
});

document.getElementById('profile-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);

    // לוג של הנתונים שנשלחים
    console.log("Form data being sent:");
    for (let pair of formData.entries()) {
        console.log(`${pair[0]}: ${pair[1]}`);
    }

    const photoFile = document.getElementById('photo-input').files[0];
    if (photoFile) {
        formData.append('profilePictureFile', photoFile);
        console.log("Photo file:", photoFile.name);
    }

    try {
        const response = await fetch('/api/users', {
            method: 'POST',
            body: formData
        });

        const data = await response.text(); // קודם נקרא את התוכן כטקסט

        if (!response.ok) {
            // יצירת אלמנט להצגת השגיאה
            const errorDiv = document.createElement('div');
            errorDiv.className = 'alert alert-danger';
            errorDiv.textContent = data; // השגיאה מהשרת

            // מחיקת הודעת שגיאה קודמת אם קיימת
            const existingError = document.querySelector('.alert-danger');
            if (existingError) {
                existingError.remove();
            }

            // הצגת השגיאה מעל הטופס
            const form = document.getElementById('profile-form');
            form.insertBefore(errorDiv, form.firstChild);

            // הסרת ההודעה אחרי 5 שניות
            setTimeout(() => {
                errorDiv.remove();
            }, 5000);

            throw new Error(data);
        }

        // בדיקה אם התגובה היא JSON
        let result;
        try {
            result = JSON.parse(data);
        } catch {
            result = data; // אם לא JSON, נשתמש בטקסט כמו שהוא
        }

        // יצירת הודעת הצלחה
        const successDiv = document.createElement('div');
        successDiv.className = 'alert alert-success';
        successDiv.textContent = 'הפרופיל נשמר בהצלחה!';

        // הצגת הודעת ההצלחה
        const form = document.getElementById('profile-form');
        form.insertBefore(successDiv, form.firstChild);

        // הסרת ההודעה אחרי 3 שניות
        setTimeout(() => {
            successDiv.remove();
        }, 3000);

    } catch (error) {
        console.error('Error details:', error);
    }
});