let pocketBaseUrl;
let loginPocketBaseInfo = {
    identity: "",
    password: "",
};
$(document).ready(function () {

    $.ajax({
        type: "GET",
        url: "/api/v1/get-value",
        success: function (data) {
            pocketBaseUrl = data.pocketBaseHost;
            loginPocketBaseInfo.identity = data.pocketBaseEmail;
            loginPocketBaseInfo.password = data.pocketBasePassword;
            authentication(loginPocketBaseInfo);
        },
        error: function (err) {
            console.log("can't get value");
        }
    });

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

            let image = $('#imageInput')[0].files[0];
            let record = new FormData();
            let product = new FormData();
            let media = new FormData();
            record.append("data", image);
            $.ajax({
                url: `${pocketBaseUrl}api/collections/image/records`,
                headers: {"Authorization": localStorage.getItem('token')},
                type: "POST",
                data: record,
                contentType: false,
                processData: false,
                error: function (err) {
                    console.log('PocketBase  error! ' + err);
                },
                success: function (response) {
                    console.log(response)

                    product.append("name", $('#name').val());
                    product.append("categoryId", $('#category').val());
                    product.append("price", $('#price').val());
                    product.append("shortDescription", $('#shortDescription').val());
                    product.append("status", $('#status').val());
                    product.append("imageId", response.id);

                    media.append("type", response.collectionName);
                    media.append("id", response.id);
                    media.append("name", response.data);

                    console.log(product);
                    saveProduct(product, media);
                    console.log(media);
                }
            });


        }
    });
})

function saveMedia(media) {
    $.ajax({
        url: '/api/v1/media',
        type: 'POST',
        data: media,
        contentType: false,
        processData: false,
        success: function (response) {
        },
        error: function (xhr) {
            showPopupError('Error!', xhr.responseJSON.message);
            deleteImage(media.imageId);
        }
    });
}

function saveProduct(product, media) {
    $.ajax({
        url: '/api/v1/product',
        type: 'POST',
        data: product,
        contentType: false,
        cache: false,
        processData: false,
        success: function (response) {
            saveMedia(media);
            showPopupSuccess('Success!', 'Add Product Success!');
        },
        error: function (xhr) {
            showPopupError('Error!', xhr.responseJSON.message);
            deleteImage(product.imageId);
        }
    });
}

function deleteImage(imageId) {
    $.ajax({
        url: `${pocketBaseUrl}api/collections/collection /records/${imageId}`,
        headers: {"Authorization": ("Admin " + localStorage.getItem('token'))},
        type: "DELETE",
        error: function (err) {
            console.log('Error!', err)
        },
        success: function (data) {
        }
    })
}

function authentication(loginInfo) {
    $.ajax({
        url: `${pocketBaseUrl}api/admins/auth-with-password`,
        type: "POST",
        data: loginInfo,
        error: function (err) {
            console.log('Error!', err);
        },
        success: function (data) {
            console.log("sss");
            localStorage.setItem("token", data.token);
        }
    });
}

function uploadImage(image) {
    $.ajax({
        url: pocketBaseUrl + "collections/" + collection + "/records",
        headers: {"Authorization": ("Admin " + localStorage.getItem('token'))},
        type: "POST",
        data: new FormData($("#formImage")[0]),
        contentType: false,
        cache: false,
        processData: false,
        error: function (err) {
            console.log('PocketBase  error! ' + err)
        },
        success: function (data) {
            let mapData = new Map(Object.entries(data))
            let postData = {
                collectionId: mapData.get("@collectionId"),
                collectionName: mapData.get("@collectionName"),
                imageId: data.id,
                imageName: data.image
            }
            saveImage(postData);
        }
    });
}

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