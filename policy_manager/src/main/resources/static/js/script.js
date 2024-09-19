$(document).ready(function() {
    // Function to fetch and highlight content
	function fetchAndHighlightContent(url, elementId) {
	    console.log('Fetching content from:', url);
	    $.get(url, function(data) {
	        console.log('Data received:', data);
	        const content = data.join('\n');
	        const codeBlock = $('#' + elementId);

	        if (codeBlock.text() !== content) {
	            codeBlock.text(content);

	            // Initialize Highlight.js
	            setTimeout(() => {
	                hljs.highlightElement(codeBlock[0]);

	                // Initialize line numbers
	                if (!codeBlock.hasClass('line-numbers-initialized')) {
	                    hljs.lineNumbersBlock(codeBlock[0]);
	                    codeBlock.addClass('line-numbers-initialized');
	                }
	            }, 100); // Adjust timeout as needed
	        }
	    }).fail(function(xhr, status, error) {
	        console.error('Failed to load content:', status, error);
	        $('#' + elementId).text('Failed to load content.');
	    });
    }

    // Function to handle the custom option visibility for each dropdown
    function handleCustomVisibility(dropdownId, customInputId, customLabelId) {
        const dropdown = document.getElementById(dropdownId);
        const customInput = document.getElementById(customInputId);
        const customLabel = document.getElementById(customLabelId);

        dropdown.addEventListener('change', function() {
            if (dropdown.value === 'custom') {
                customInput.style.display = 'inline';
                customLabel.style.display = 'inline';
            } else {
                customInput.style.display = 'none';
                customLabel.style.display = 'none';
            }
        });

        // Ensure that custom input is hidden initially
        customInput.style.display = 'none';
        customLabel.style.display = 'none';
    }

    // Initialize custom visibility handlers for each dropdown
    handleCustomVisibility('actionId', 'customActionId', 'customActionIdLabel');
    handleCustomVisibility('profileId', 'customProfileId', 'customProfileIdLabel');
    handleCustomVisibility('vmType', 'customVmType', 'customVmTypeLabel');

    // Generate request based on selected or custom input values
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
        const requestName = "Online_Generated"; // Adjust this name as needed

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

        if (vmType === 'custom' && customVmType) {
            request += `    (resource/vm-type, "${customVmType}")\n`;
        } else if (vmType && vmType !== 'nothing') {
            request += `    (resource/vm-type, "${vmType}")\n`;
        }

        if (vmName) {
            request += `    (resource/vm-name, "${vmName}")\n`;
        }

        request += `}`;
        
        // Log the request to the console for debugging
        console.log('Generated request:', request);

        $.ajax({
            url: '/requests/validate',
            type: 'POST',
            data: request,
            contentType: 'text/plain',
			unction(response) {
			        alert('Request successfully validated: ' + response); // Alert for success
			        fetchAndHighlightContent('/requests', 'requestsContent'); // Update requests content

			        // Reset dropdown menus
			        $('#actionId').val('default');
			        $('#profileId').val('default');
			        $('#vmType').val('default');
			},
            error: function(xhr, status, error) {
                console.error('Error submitting request:', xhr.responseText);
                alert('Error submitting request: ' + xhr.responseText); // Alert for error
            }
        });
        $('#requestForm')[0].reset();
    });

    fetchAndHighlightContent('/policies', 'policiesContent');
    fetchAndHighlightContent('/requests', 'requestsContent');

    $('#validatePolicies').click(function() {
        var policies = $('#policiesText').val().split('\n');
        if (policies.length === 0 || policies[0].trim() === '') {
            alert('Please enter policies.');
            return;
        }

        console.log('Submitting policies:', policies);
        $.ajax({
            url: '/policies/validate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(policies),
            success: function(response) {
				alert('RequesPolicy successfully validated: ' + response); // Alert for success
                fetchAndHighlightContent('/policies', 'policiesContent'); // Update policies content
            },
            error: function(xhr, status, error) {
                console.error('Error updating policies:', xhr.responseText);
                alert('Error updating policies: ' + xhr.responseText);
            }
        });
    });
	
	$('#submitRequestBtn').click(function() {
	   $.ajax({
	       url: '/requests/submit',
	       type: 'POST',
	       success: function(response) {
			   alert('Request successfully submitted: ' + response); // Alert for success
	       },
	       error: function(xhr, status, error) {
	           console.error('Error submitting request:', xhr.responseText);
	           alert('Error submitting policies: ' + xhr.responseText);
	       }
	   });
	});
	
	$('#submitPolicyBtn').click(function() {
		   $.ajax({
		       url: '/policies/submit',
		       type: 'POST',
		       success: function(response) {
				   alert('Policies successfully submitted: ' + response); // Alert for success
		       },
		       error: function(xhr, status, error) {
		           console.error('Error submitting policies:', xhr.responseText);
		           alert('Error submitting policies: ' + xhr.responseText);
		       }
		   });
		});
});
