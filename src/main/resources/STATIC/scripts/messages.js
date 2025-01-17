// messages.js
document.getElementById('messageForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const formData = new FormData(this);
    fetch('/api/messages', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(() => {
        loadMessages();
        this.reset();
    });
});

function loadMessages() {
    fetch('/api/messages/my-messages')
        .then(response => response.json())
        .then(messages => {
            const messagesList = document.getElementById('messagesList');
            messagesList.innerHTML = messages.map(message => `
                <div class="message">
                    <h3>${message.subject}</h3>
                    <p>${message.content}</p>
                    <span class="status ${message.status.toLowerCase()}">${message.status}</span>
                </div>
            `).join('');
        });
}

// only for admin
function updateStatus(messageId, status) {
    fetch(`/api/messages/${messageId}/status?status=${status}`, {
        method: 'PATCH'
    }).then(() => loadMessages());
}

// first load
loadMessages();