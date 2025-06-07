document.addEventListener('DOMContentLoaded', function() {
    const cancelButton = document.querySelectorAll('.cancel-button');
    const addAppointmentBtn = document.getElementById('addAppointmentBtn');
    const appointmentsList = document.querySelector('.appointments-list');

    cancelButton.forEach(button => {
        button.addEventListener('click', function() {
            const card = this.closest('.appointment-card');
            if (card) {
                alert(`Agendamento de ${card.querySelector('.client-name').textContent} cancelado!`);
                card.remove();
            }
        });
    });

    const dateFilter = document.getElementById('dateFilter');
    dateFilter.addEventListener('change', function() {
        const selectedDate = this.value;
        const appointmentCards = document.querySelectorAll('.appointment-card');

        appointmentCards.forEach(card => {
            // Aqui você implementaria a lógica de filtragem por data
            console.log(`Filtrando por: ${selectedDate}`);
        });
    });

    addAppointmentBtn.addEventListener('click', function() {
        // Por enquanto, vamos apenas adicionar um novo card estático
        const newCard = document.createElement('div');
        newCard.classList.add('appointment-card');
        newCard.innerHTML = `
            <div class="appointment-info">
                <h2 class="client-name">NOVO AGENDAMENTO</h2>
                <p class="service">Serviço Pendente - Horário</p>
                <p class="status pending">Status: Pendente</p>
            </div>
            <div class="appointment-actions">
                <button class="confirm-button">
                    <span class="checkmark">&#10004;</span> Confirmar
                </button>
                <button class="cancel-button">Cancelar</button>
            </div>
        `;
        appointmentsList.appendChild(newCard);

        // É importante adicionar os event listeners para os novos botões
        const newCancelButton = newCard.querySelector('.cancel-button');
        newCancelButton.addEventListener('click', function() {
            const card = this.closest('.appointment-card');
            if (card) {
                alert(`Agendamento de ${card.querySelector('.client-name').textContent} cancelado!`);
                card.remove();
            }
        });

        const newConfirmButton = newCard.querySelector('.confirm-button');
        newConfirmButton.addEventListener('click', function() {
            const card = this.closest('.appointment-card');
            const statusElement = card.querySelector('.status');
            statusElement.textContent = 'Status: Confirmado';
            statusElement.classList.remove('pending');
            statusElement.classList.add('confirmed');
            this.disabled = true;
            this.style.opacity = '0.8';
            this.style.cursor = 'not-allowed';
        });
    });
});