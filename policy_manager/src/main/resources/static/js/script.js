$('#generateRequestBtn').click(function() {
    console.log('Generate Request button clicked'); // Debugging statement

    // Get values from dropdowns and input fields
    const actionId = $('#actionId').val();
    const customActionId = $('#customActionId').val().trim();

    const profileId = $('#profileId').val();
    const customProfileId = $('#customProfileId').val().trim();

    const vmType = $('#vmType').val();
    const customVmType = $('#customVmType').val().trim();

    const vmName = $('#vmName').val().trim();

    // Predetermined name for the request
    const requestName = "Online_Generated";

    // Construct the request body with parameters
    let request = `Request: { ${requestName}\n`;

    // Handle Action ID (predefined, custom, or nothing)
    if (actionId === 'custom' && customActionId) {
        request += `    (action/action-id, "${customActionId}")\n`;
    } else if (actionId && actionId !== 'nothing') {
        request += `    (action/action-id, "${actionId}")\n`;
    }

    // Handle Profile ID (predefined, custom, or nothing)
    if (profileId === 'custom' && customProfileId) {
        request += `    (subject/profile-id, "${customProfileId}")\n`;
    } else if (profileId && profileId !== 'nothing') {
        request += `    (subject/profile-id, "${profileId}")\n`;
    }

    // Handle VM Type (predefined, custom, or nothing)
    if (vmType === 'custom' && customVmType) {
        request += `    (resource/vm-type, "${customVmType}")\n`;
    } else if (vmType && vmType !== 'nothing') {
        request += `    (resource/vm-type, "${vmType}")\n`;
    }

    // Handle VM Name (optional)
    if (vmName) {
        request += `    (resource/vm-name, "${vmName}")\n`;
    }

    // Close the request block
    request += `}`;

    // Log the request to the console for debugging
    console.log('Generated request:', request);

    // Submit the request via AJAX
    $.ajax({
        url: '/requests/validate',
        type: 'POST',
        data: request,
        contentType: 'text/plain',
        success: function(response) {
            alert('Request successfully validated: ' + response); // Alert for success
            fetchAndHighlightContent('/requests', 'requestsContent'); // Update requests content

            // Reset form fields after submission
            $('#requestForm')[0].reset();
            
            // Reset dropdowns to default option
            $('#actionId').prop('selectedIndex', 0);
            $('#profileId').prop('selectedIndex', 0);
            $('#vmType').prop('selectedIndex', 0);

            // Hide custom input fields
            $('#customActionId').hide();
            $('#customActionIdLabel').hide();
            $('#customProfileId').hide();
            $('#customProfileIdLabel').hide();
            $('#customVmType').hide();
            $('#customVmTypeLabel').hide();
        },
        error: function(xhr, status, error) {
            console.error('Error submitting request:', xhr.responseText);
            alert('Error submitting request: ' + xhr.responseText); // Alert for error
        }
    });
});
