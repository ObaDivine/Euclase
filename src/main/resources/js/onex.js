function verifyMobileNumber(mobileNumber) {
    $.ajax({
        url: "/onex/otp/mobile/" + mobileNumber,
        type: "GET",
        success: function (data) {
            $('#mobileNumberVerificationOTP').show();
            $('#otp').focus();
        },
        error: function (xhr, status) {
            alert(xhr);
        }
    });
}
;

function fetchDataPlan() {
    let telco = document.getElementById("telco").value;
    $.ajax({
        url: "/onex/data/plan/" + telco,
        type: "GET",
        dataType: "json",
        success: function (data) {
            var html = '<option value="">---Select ' + telco + ' Data Plan ---</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i].subscriptionCode + '|' + data[i].subscriptionName + '|' + data[i].subscriptionAmount + '">' + data[i].subscriptionName + '</option>';
            }
            html += '</option>';
            $('#subscriptionCode').html(html);
        },
        error: function (xhr, status) {
            alert(xhr);
        }
    });
}
;

function smartcardLookup() {
    let biller = document.getElementById("biller").value;
    let smartcard = document.getElementById("smartcard").value;
    if (biller !== "" && smartcard !== "") {
        $.ajax({
            url: "/onex/cabletv/lookup/" + biller + "/" + smartcard,
            type: "GET",
            dataType: "json",
            success: function (data) {
                $('#customerName').text(data.customerName + ' - ' + data.customerNumber);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

function bouquetLookup() {
    let biller = document.getElementById("biller").value;
    //Fetch the Subscriptions
    $.ajax({
        url: "/onex/cabletv/subscription/" + biller,
        type: "GET",
        dataType: "json",
        success: function (data) {
            var html = '<option value="">---Select ' + biller + ' Package ---</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i].subscriptionCode + '|' + data[i].subscriptionName + '|' + data[i].subscriptionAmount + '">' + data[i].subscriptionName + '</option>';
            }
            html += '</option>';
            $('#bouquet').html(html);
        },
        error: function (xhr, status) {
            alert(xhr);
        }
    });
}
;

function meterLookup() {
    let disco = document.getElementById("disco").value;
    let meterNumber = document.getElementById("meterNumber").value;
    let billType = document.getElementById("billType").value;
    if (disco !== "" && meterNumber !== "" && billType !== "") {
        $.ajax({
            url: "/onex/electricity/lookup/" + disco + "/" + billType + "/" + meterNumber,
            type: "GET",
            dataType: "json",
            success: function (data) {
                $('#customerName').text(data.customerName);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

$('.digit-group').find('input').each(function () {
    $(this).attr('maxlength', 1);
    $(this).on('keyup', function (e) {
        var parent = $($(this).parent());

        if (e.keyCode === 8 || e.keyCode === 37) {
            var prev = parent.find('input#' + $(this).data('previous'));

            if (prev.length) {
                $(prev).select();
            }
        } else if ((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 65 && e.keyCode <= 90) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode === 39) {
            var next = parent.find('input#' + $(this).data('next'));

            if (next.length) {
                $(next).select();
            } else {
                if (parent.data('autosubmit')) {
                    parent.submit();
                }
            }
        }
    });
});

function preloaderFunction() {
    var myVar = setTimeout(showPage);

}
;

function showPage() {
    document.getElementById('loader').style.display = "none";
    document.getElementById('top').style.display = "block";
}
;

function deleteTransferBeneficiary(id) {
    $('#deleteTransferBeneficiary').attr("href", "/onex/funds-transfer/beneficiary/delete/" + id);
    $('#id').val(id);
    $('#transferBeneficiaryDeleteModal').modal('show');
}
;

function updateTwoFactorAuth() {
    $('#confirmTwoFactorAuth').modal('show');
}
var policyList = new List('dealsTable', {
    valueNames: ["telco", "mobileNumber", "amount", "reference", "status", "subscriptionName", "bouquet", "smartcard", "viewingMonth", "biller", "disco", "billType", "meterNumber", "token", "sourceAccount", "sourceAccountName", "sourceBank", "beneficiaryAccount", "beneficiaryAccountName", "beneficiaryBank",
        "date", "walletId", "transType", "openingBalance", "closingBalance", "narration", "dataPlan", "accountNumber", "accountName", "bankName", "mnemonic"
    ],
    page: 10,
    pagination: true
});

function deleteScheduleAirtime(id) {
    $('#deleteId').val(id);
    $('#deleteScheduleAirtimeModal').modal('show');
}
;

function updateScheduleAirtimeStatus(id) {
    $('#updateId').val(id);
    $('#updateScheduleAirtimeStatusModal').modal('show');
}
;

function deleteScheduleData(id) {
    $('#deleteId').val(id);
    $('#deleteScheduleDataModal').modal('show');
}
;

function updateScheduleDataStatus(id) {
    $('#updateId').val(id);
    $('#updateScheduleDataStatusModal').modal('show');
}
;

function deleteScheduleCableTv(id) {
    $('#deleteId').val(id);
    $('#deleteScheduleCableTvModal').modal('show');
}
;

function updateScheduleCableTvStatus(id) {
    $('#updateId').val(id);
    $('#updateScheduleCableTvStatusModal').modal('show');
}
;

function deleteScheduleElectricity(id) {
    $('#deleteId').val(id);
    $('#deleteScheduleElectricityModal').modal('show');
}
;

function updateScheduleElectricityStatus(id) {
    $('#updateId').val(id);
    $('#updateScheduleElectricityStatusModal').modal('show');
}
;

function deleteScheduleFundsTransfer(id) {
    $('#deleteId').val(id);
    $('#deleteScheduleFundsTransferModal').modal('show');
}
;

function updateScheduleFundsTransferStatus(id) {
    $('#updateId').val(id);
    $('#updateScheduleFundsTransferStatusModal').modal('show');
}
;

function password_show_hide() {
    var x = document.getElementById("password");
    var show_eye = document.getElementById("show_eye");
    var hide_eye = document.getElementById("hide_eye");
    hide_eye.classList.remove("d-none");
    if (x.type === "password") {
        x.type = "text";
        show_eye.style.display = "none";
        hide_eye.style.display = "block";
    } else {
        x.type = "password";
        show_eye.style.display = "block";
        hide_eye.style.display = "none";
    }
}
;

function extractDataSubAmount() {
    var subscriptionText = document.getElementById('subscriptionCode').value;
    var textArray = subscriptionText.split("|");
    let formatedAmount = formatAmount(textArray[2]);
    $('#dataSubAmount').text(formatedAmount);
}
;


function extractCableSubAmount() {
    var subscriptionText = document.getElementById('bouquet').value;
    var textArray = subscriptionText.split("|");
    let formatedAmount = formatAmount(textArray[2]);
    $('#cableTvSubAmount').text(formatedAmount);
}
;

function formatAmount(amountToFormat) {
    var format = new Intl.NumberFormat('en-NG', {
        style: 'currency',
        currency: 'NGN',
        minimumFractionDigits: 2
    });

    return format.format(amountToFormat);
}

