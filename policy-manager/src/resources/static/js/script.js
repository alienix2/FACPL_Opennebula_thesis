$(document).ready(function() {
    // Load policies and requests on page load
    $.get("/policies", function(data) {
        $("#policiesContent").val(data.join("\n"));
    }).fail(function() {
        $("#policiesContent").val("Failed to load policies.");
    });

    $.get("/requests", function(data) {
        $("#requestsContent").val(data.join("\n"));
    }).fail(function() {
        $("#requestsContent").val("Failed to load requests.");
    });

    // Submit request
    $("#submitRequest").click(function() {
        var request = $("#requestText").val();
        if (request.trim() === "") {
            alert("Please enter a request.");
            return;
        }

        $.ajax({
            url: "/submit-request",
            type: "POST",
            data: request,
            contentType: "text/plain",
            success: function(response) {
                alert(response);
                // Refresh requests content after submission
                $.get("/requests", function(data) {
                    $("#requestsContent").val(data.join("\n"));
                });
            },
            error: function(xhr, status, error) {
                alert("Error submitting request: " + error);
            }
        });
    });

    // Update policies
    $("#updatePolicies").click(function() {
        var policies = $("#policiesText").val().split("\n");
        if (policies.length === 0 || policies[0].trim() === "") {
            alert("Please enter policies.");
            return;
        }

        $.ajax({
            url: "/update-policies",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(policies),
            success: function(response) {
                alert(response);
                // Refresh policies content after update
                $.get("/policies", function(data) {
                    $("#policiesContent").val(data.join("\n"));
                });
            },
            error: function(xhr, status, error) {
                alert("Error updating policies: " + error);
            }
        });
    });
});
