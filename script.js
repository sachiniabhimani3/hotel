// Function to validate the booking form
function validateBookingForm() {
    var checkinDate = document.getElementById('checkin-date').value;
    var checkoutDate = document.getElementById('checkout-date').value;
    var roomType = document.getElementById('room-type').value;
    var guests = document.getElementById('guests').value;

    if (!checkinDate || !checkoutDate || !roomType || !guests) {
        alert('Please fill in all fields.');
        return false;
    }

    if (new Date(checkinDate) >= new Date(checkoutDate)) {
        alert('Check-out date must be after the check-in date.');
        return false;
    }

    if (parseInt(guests) < 1) {
        alert('Number of guests must be at least 1.');
        return false;
    }

    // Add more validation as needed

    return true; // form is valid
}

// Event listener for form submission
document.getElementById('booking-form').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the default form submission

    if (validateBookingForm()) {
        // To be implemented
        this.submit();

        // To be implemented
    }
});

// Example AJAX function to send data to the server (if needed)
function sendDataToServer() {
    var formData = new FormData(document.getElementById('booking-form'));

    fetch('/path-to-your-server-endpoint', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log(data); // Handle the response data
    })
    .catch(error => {
        console.error('Error:', error); // Handle any errors
    });
}
