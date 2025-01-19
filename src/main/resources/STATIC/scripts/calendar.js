// קביעת כתובת ה-API לפי הסביבה
const API_URL = window.location.hostname === 'localhost'
    ? 'http://localhost:8080'
    : 'https://a833-2a00-a041-24a2-5200-5094-6d5e-6fe2-c70d.ngrok-free.app';  // החלף עם כתובת ה-ngrok שלך

// משתנים גלובליים
let currentEvent = null;
let selectedDate;
let calendar;
const userId = 1;

// פונקציות תקשורת עם ה-API
async function fetchEvents() {
    try {
        const response = await fetch(`${API_URL}/api/events`);
        const events = await response.json();
        return events.map(event => ({
            id: event.eventId,
            title: event.title,
            start: new Date(event.startDate),
            end: new Date(event.endDate),
            className: 'event-' + event.status.toLowerCase(),
            currentParticipants: event.currentParticipants,
            maxParticipants: event.maxParticipants,
            location: event.location,
            status: event.status,
            participantIds: event.participants ? event.participants.map(p => p.userId) : []
        }));
    } catch (error) {
        console.error('Error fetching events:', error);
        return [];
    }
}

async function createEvent(eventData) {
    try {
        console.log('Sending event data:', eventData);
        const response = await fetch(`${API_URL}/api/events`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(eventData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Server response:', errorText);
            throw new Error(errorText);
        }

        return await response.json();
    } catch (error) {
        console.error('Error creating event:', error);
        throw error;
    }
}

async function updateEvent(eventId, eventData) {
    const response = await fetch(`${API_URL}/api/events/${eventId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(eventData)
    });
    return await response.json();
}

async function deleteEvent(eventId) {
    await fetch(`${API_URL}/api/events/${eventId}`, {
        method: 'DELETE'
    });
}

async function registerForEvent(eventId, userId) {
    try {
        const response = await fetch(`${API_URL}/api/rsvp`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({eventId: eventId, userId: userId})
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Registration failed');
        }

        return await response.json();
    } catch (error) {
        console.error('Registration error:', error);
        throw error;
    }
}

async function checkRegistrationStatus(eventId, userId) {
    try {
        const response = await fetch(`${API_URL}/api/rsvp/status?eventId=${eventId}&userId=${userId}`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('שגיאה בבדיקת סטטוס הרשמה:', error);
        return null;
    }
}

async function cancelEventRegistration(eventId, userId) {
    try {
        const response = await fetch(`${API_URL}/api/rsvp/cancel?eventId=${eventId}&userId=${userId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to cancel registration');
        }

        return true;

    } catch (error) {
        console.error('Error canceling registration:', error);
        throw error;
    }
}

// פונקציות עזר
function showToast(message, duration = 3000) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.style.display = 'block';

    setTimeout(() => {
        toast.style.display = 'none';
    }, duration);
}

function updateEventModalInfo(event, isRegistered) {
    const modalContent = document.querySelector('.modal-content');
    const participantsInfo = modalContent.querySelector('.participants-info') || document.createElement('div');
    participantsInfo.className = 'participants-info';

    const maxParticipants = event.extendedProps.maxParticipants;
    const currentParticipants = event.extendedProps.currentParticipants || 0;

    participantsInfo.innerHTML = `
        <p>משתתפים: ${currentParticipants}${maxParticipants ? `/${maxParticipants}` : ' (ללא הגבלה)'}</p>
        ${isRegistered ? '<p class="registered-status">✓ רשום לאירוע</p>' : ''}
    `;

    if (!modalContent.querySelector('.participants-info')) {
        modalContent.appendChild(participantsInfo);
    }
}

// הגדרת לוח השנה
document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'he',
        direction: 'rtl',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        editable: true,
        selectable: true,
        dayMaxEvents: true,
        events: fetchEvents,

        eventDataTransform: function (eventData) {
            return {
                ...eventData,
                extendedProps: {
                    currentParticipants: eventData.currentParticipants,
                    maxParticipants: eventData.maxParticipants,
                    location: eventData.location
                }
            };
        },

        select: function (info) {
            currentEvent = null;
            document.getElementById('modalTitle').textContent = 'הוסף אירוע חדש';
            document.getElementById('submitBtn').textContent = 'הוסף אירוע';
            document.getElementById('deleteBtn').style.display = 'none';
            document.getElementById('attendBtn').style.display = 'none';
            document.getElementById('eventForm').reset();

            selectedDate = info.start;
            document.getElementById('eventModal').style.display = 'block';
        },eventClick: async function (info) {
            currentEvent = info.event;
            const userId = 1;

            const registrationStatus = await checkRegistrationStatus(currentEvent.id, userId);
            const isRegistered = registrationStatus && registrationStatus.id;

            document.getElementById('modalTitle').textContent = 'פרטי אירוע';
            document.getElementById('submitBtn').textContent = 'עדכן אירוע';
            document.getElementById('deleteBtn').style.display = 'inline-flex';
            document.getElementById('eventLocation').value = currentEvent.extendedProps.location || '';

            const attendBtn = document.getElementById('attendBtn');
            attendBtn.style.display = 'block';
            const maxParticipants = currentEvent.extendedProps.maxParticipants;
            const currentParticipants = currentEvent.extendedProps.currentParticipants;

            if (isRegistered) {
                attendBtn.textContent = 'בטל הרשמה';
                attendBtn.className = 'btn cancel';
                attendBtn.disabled = false;
            } else if (maxParticipants === null || currentParticipants < maxParticipants) {
                attendBtn.textContent = 'הרשם לאירוע';
                attendBtn.className = 'btn register';
                attendBtn.disabled = false;
            } else {
                attendBtn.textContent = 'האירוע מלא';
                attendBtn.className = 'btn disabled';
                attendBtn.disabled = true;
            }

            const formatTime = (date) => {
                return date ? date.toLocaleTimeString('he-IL', {hour: '2-digit', minute: '2-digit'}) : '';
            };
            document.getElementById('eventTitle').value = currentEvent.title || '';
            document.getElementById('eventStart').value = formatTime(currentEvent.start);
            document.getElementById('eventEnd').value = formatTime(currentEvent.end);
            document.getElementById('eventLocation').value = currentEvent.extendedProps.location || '';

            updateEventModalInfo(currentEvent, isRegistered);

            document.getElementById('eventModal').style.display = 'block';
        },

        eventDrop: async function (info) {
            try {
                await updateEvent(info.event.id, {
                    title: info.event.title,
                    startDate: info.event.start,
                    endDate: info.event.end,
                    location: info.event.extendedProps.location
                });
                showToast('האירוע הועבר בהצלחה');
            } catch (error) {
                console.error('שגיאה בעדכון האירוע:', error);
                info.revert();
                showToast('שגיאה בעדכון האירוע');
            }
        },

        eventResize: async function (info) {
            try {
                await updateEvent(info.event.id, {
                    startDate: info.event.start,
                    endDate: info.event.end,
                    location: info.event.extendedProps.location
                });
                showToast('משך האירוע עודכן בהצלחה');
            } catch (error) {
                console.error('שגיאה בעדכון משך האירוע:', error);
                info.revert();
                showToast('שגיאה בעדכון משך האירוע');
            }
        }
    });

    // טיפול בטופס האירוע
    document.getElementById('eventForm').onsubmit = async function (e) {
        e.preventDefault();

        const title = document.getElementById('eventTitle').value;
        const startTime = document.getElementById('eventStart').value;
        const endTime = document.getElementById('eventEnd').value;
        const location = document.getElementById('eventLocation').value;

        if (!location) {
            showToast('נא להזין מיקום לאירוע');
            return;
        }

        const maxParticipantsInput = document.getElementById('maxParticipants');
        const maxParticipants = maxParticipantsInput.value.trim().toLowerCase() === 'אין הגבלה'
            ? null
            : parseInt(maxParticipantsInput.value, 10);

        const eventDate = currentEvent ? currentEvent.start : selectedDate;
        const startDate = new Date(eventDate);
        const endDate = new Date(eventDate);

        const [startHours, startMinutes] = startTime.split(':');
        const [endHours, endMinutes] = endTime.split(':');

        startDate.setHours(parseInt(startHours), parseInt(startMinutes));
        endDate.setHours(parseInt(endHours), parseInt(endMinutes));

        const eventData = {
            title,
            startDate,
            endDate,
            maxParticipants,
            location
        };

        try {
            if (currentEvent) {
                await updateEvent(currentEvent.id, eventData);
                showToast('האירוע עודכן בהצלחה');
            } else {
                await createEvent(eventData);
                showToast('האירוע נוצר בהצלחה');
            }
            calendar.refetchEvents();
        } catch (error) {
            console.error('שגיאה בשמירת האירוע:', error);
            showToast(error.message || 'שגיאה בשמירת האירוע');
        }

        document.getElementById('eventModal').style.display = 'none';
    };

    // טיפול בכפתור מחיקה
    document.getElementById('deleteBtn').onclick = async function () {
        if (currentEvent && confirm('האם אתה בטוח שברצונך למחוק את האירוע?')) {
            try {
                await deleteEvent(currentEvent.id);
                showToast('האירוע נמחק בהצלחה');
                calendar.refetchEvents();
                document.getElementById('eventModal').style.display = 'none';
                currentEvent = null;
            } catch (error) {
                console.error('שגיאה במחיקת האירוע:', error);
                showToast('שגיאה במחיקת האירוע');
            }
        }
    };

    // טיפול בכפתור הרשמה
    document.getElementById('attendBtn').onclick = async function () {
        if (!currentEvent) return;

        try {
            const userId = 1;
            const registrationStatus = await checkRegistrationStatus(currentEvent.id, userId);
            const isRegistered = registrationStatus && registrationStatus.id;

            if (isRegistered) {
                await cancelEventRegistration(currentEvent.id, userId);
                showToast('ההרשמה בוטלה בהצלחה');
            } else {
                await registerForEvent(currentEvent.id, userId);
                showToast('נרשמת בהצלחה לאירוע');
            }

            if (calendar) {
                await fetchEvents();
                calendar.refetchEvents();
            }

            document.getElementById('eventModal').style.display = 'none';
        } catch (error) {
            console.error('שגיאה בטיפול בהרשמה:', error);
            showToast(error.message || 'שגיאה בטיפול בהרשמה');
        }
    };

    // טיפול בסגירת מודאל
    document.querySelector('.close').onclick = function () {
        document.getElementById('eventModal').style.display = 'none';
    };

    window.onclick = function (event) {
        if (event.target.classList.contains('modal')) {
            document.getElementById('eventModal').style.display = 'none';
        }
    };

    // הוספת סטיילים לכפתורים
    const style = document.createElement('style');
    style.textContent = `
        .btn.register { background-color: #4CAF50; }
        .btn.cancel { background-color: #f44336; }
        .btn.disabled { background-color: #cccccc; cursor: not-allowed; }
        .registered-status { color: #4CAF50; font-weight: bold; }
    `;
    document.head.appendChild(style);

    calendar.render();
});

async function sendEmergencyEmail() {
    try {
        const response = await fetch(`${API_URL}/api/email/emergency`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                subject: 'נושא החירום',
                message: 'נא לשים לב להודעה זו ולעדכן את המערכת בהתאם'
            })
        });

        if (!response.ok) {
            throw new Error('Failed to send emergency email');
        }

        showToast('הודעת החירום נשלחה בהצלחה');
    } catch (error) {
        console.error('שגיאה בשליחת הודעת חירום:', error);
        showToast('שגיאה בשליחת הודעת חירום');
    }
}

// חיבור לכפתור חירום
document.getElementById('emergencyButton').onclick = sendEmergencyEmail;

// התאמות למובייל
if (window.matchMedia("(max-width: 390px)").matches) {
    const highlight = document.querySelector('.highlight');
    const navItems = document.querySelectorAll('.nav-item');

    function moveHighlight(element) {
        const rect = element.getBoundingClientRect();
        const navRect = document.querySelector('.bottom-nav').getBoundingClientRect();

        const x = rect.left - navRect.left + (rect.width - 40) / 2;
        const y = rect.top - navRect.top + (rect.height - 40) / 2;

        highlight.style.transform = `translate(${x}px, ${y}px)`;
    }

    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            navItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');
            moveHighlight(item);
        });
    });

    // הצבת ההדגשה הראשונית
    const activeItem = document.querySelector('.nav-item.active');
    if (activeItem) {
        moveHighlight(activeItem);
    }
}