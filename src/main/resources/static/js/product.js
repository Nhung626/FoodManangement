$(document).ready(function () {
    $('.close').click(function () {
        $('#popup').hide();
        clearForm();
    });

    $('#imageInput').on('change', function (event) {
        let input = event.target;
        let preview = $('#imagePreview')[0];

        if (input.files && input.files[0]) {
            let reader = new FileReader();
            reader.onload = function (e) {
                $(preview).attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
        } else {
            $(preview).attr('src', '#');
        }
    });

    $('#formProduct').validate({
        rules: {
            name: {
                required: true,
                minlength: 6,
                maxlength: 12
            },
            price: {
                required: true,
                number: true,
                min: 0
            },
            category: {
                required: true
            },
            shortDescription: {
                required: true
            },
            status: {
                required: true
            }
        },
        messages: {
            name: {
                required: 'Please enter the name',
                minlength: 'Please enter at least 6 characters',
                maxlength: 'Please enter no more than 12 characters'
            },
            price: {
                required: 'Please enter the price',
                number: 'Please enter a valid number',
                min: 'Please enter a value greater than or equal to 0'
            },
            category: {
                required: 'Please select a category'
            },
            shortDescription: {
                required: 'Please enter a short description'
            },
            status: {
                required: 'Please select a status'
            }
        },
        submitHandler: function (form) {
            let formData = new FormData();
            let image = $('#imageInput')[0].files[0];
            formData.append("image", image);
            formData.append("name", $('#name').val())
            formData.append("category", $('#category').val());
            formData.append("price", $('#price').val());
            formData.append("shortDescription", $('#shortDescription').val());
            formData.append("status", $('#status').val());

            $.ajax({
                url: '/product',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    showPopupSuccess('Success!', 'Add Product Success!');
                },
                error: function (xhr) {
                    showPopupError('Error!', xhr.responseJSON.message)
                }
            });
        }
    });

    function showPopupSuccess(title, message) {
        $('#popup .modal-header h2').text(title);
        $('#popup .modal-header h2').css({
            'color': 'green'
        })
        $('#popup .modal-body h3').text(message);
        $('#popup').show();
    }

    function showPopupError(title, message) {
        $('#popup .modal-header h2').text(title);
        $('#popup .modal-header h2').css({
            'color': 'red'
        })
        $('#popup .modal-body h3').text(message);
        $('#popup').show();
    }

    function clearForm() {
        $("#formProduct input, #formProduct textarea")
            .not("#btnAddProduct").each(function () {
            $(this).val("");
        });
        $("#imageInput").val("");
        $("#imagePreview").attr("src", "#");
    }
})