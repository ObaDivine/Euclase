var list = new List('listTable', {
    valueNames: ['id', 'date', 'createdBy', 'document', 'priority', 'sla', 'comment', 'username', 'name', 'mobile', 'email', 'gender', 'dob',
        'branch', 'department', 'unit', 'designation', 'grade', 'code', 'location', 'position', 'type', 'tag', 'hod', 'teamLead', 'branchHead',
        'version', 'link', 'frequency', 'password', 'directory', 'access', 'expiry', 'violated', 'startDate', 'endDate', 'online', 'address',
        'daily', 'weekly', 'monthly', 'cost', 'band', 'url', 'channel', 'balanceBf', 'closingBalance', 'company', 'session', 'copy', 'subject', 'error', 'count',
        'old', 'new', 'source', 'destination', 'time', 'to', 'from', 'accessedBy'
    ],
    page: 10,
    pagination: true
});

const viewAll = document.querySelector('[data-list-view="*"]');
const viewLess = document.querySelector('[data-list-view="less"]');
let totalItem = list.items.length;
let pageQuantity = Math.ceil(list.size() / list.page);
let pageCount = 1;
const itemsPerPage = list.page;

const toggleViewBtn = () => {
    viewLess.classList.toggle('d-none');
    viewAll.classList.toggle('d-none');
};

if (viewAll) {
    viewAll.addEventListener('click', () => {
        list.show(1, totalItem);
        pageCount = 1;
        toggleViewBtn();
    });
}

if (viewLess) {
    viewLess.addEventListener('click', () => {
        list.show(1, itemsPerPage);
        pageCount = 1;
        toggleViewBtn();
    });
}

$(document).ready(function () {
    var navbarTopStyle = window.config.config.phoenixNavbarTopStyle;
    var navbarTop = document.querySelector('.navbar-top');
    if (navbarTopStyle === 'darker') {
        navbarTop.classList.add('navbar-darker');
    }

    var navbarVerticalStyle = window.config.config.phoenixNavbarVerticalStyle;
    var navbarVertical = document.querySelector('.navbar-vertical');
    if (navbarVertical && navbarVerticalStyle === 'darker') {
        navbarVertical.classList.add('navbar-darker');
    }
    ;
});

var navbarStyle = window.config.config.phoenixNavbarStyle;
if (navbarStyle && navbarStyle !== 'transparent') {
    document.querySelector('body').classList.add(`navbar-${navbarStyle}`);
}
;

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
;

function filterUnits(deptCode) {
    if (deptCode !== '') {
        $('#departmentUnitCode').find('option:contains(' + deptCode + ')').hide();
    }
}
;

$("#finalSubmit").click(function () {
    var jsonArray = $("#form1, #form2, #form3").serializeArray();
    const json = {};
    $.each(jsonArray, function () {
        json[this.name] = this.value || "";
    });
    $('#serializedForm').val(JSON.stringify(json));
});

function fetchCompanyBranches() {
    let company = document.getElementById("company").value;
    if (company !== "") {
        $.ajax({
            url: "/euclase/setup/company/branch/" + company,
            type: "GET",
            dataType: "json",
            success: function (data) {
                var html = '<option value="">---Select branch---</option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].branchName + '</option>';
                }
                html += '</option>';
                $('#branch').html(html);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

function fetchBranchDepartment() {
    let department = document.getElementById("branch").value;
    if (department !== "") {
        $.ajax({
            url: "/euclase/setup/branch/department/" + department,
            type: "GET",
            dataType: "json",
            success: function (data) {
                var html = '<option value="">---Select department---</option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].departmentName + '</option>';
                }
                html += '</option>';
                $('#department').html(html);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

function fetchDepartmentUnits() {
    let unit = document.getElementById("department").value;
    if (unit !== "") {
        $.ajax({
            url: "/euclase/setup/department/unit/" + unit,
            type: "GET",
            dataType: "json",
            success: function (data) {
                var html = '<option value="">---Select department unit---</option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].departmentUnitName + '</option>';
                }
                html += '</option>';
                $('#departmentUnit').html(html);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

function fetchCompanyDocumentGroup() {
    let company = document.getElementById("company").value;
    if (company !== "") {
        $.ajax({
            url: "/euclase/setup/company/document/group/" + company,
            type: "GET",
            dataType: "json",
            success: function (data) {
                var html = '<option value="">---Select document group---</option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].documentGroupName + '</option>';
                }
                html += '</option>';
                $('#documentGroup').html(html);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

function fetchDocumentGroupType() {
    let documentGroup = document.getElementById("documentGroup").value;
    if (documentGroup !== "") {
        $.ajax({
            url: "/euclase/setup/document/group/type/" + documentGroup,
            type: "GET",
            dataType: "json",
            success: function (data) {
                var html = '<option value="">---Select document type---</option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].documentTypeName + '</option>';
                }
                html += '</option>';
                $('#documentType').html(html);
            },
            error: function (xhr, status) {
                alert(xhr);
            }
        });
    }
}
;

function documentAccess(documentId) {
    $.ajax({
        url: "/euclase/document/access/" + documentId,
        type: "GET",
        dataType: "text",
        error: function (xhr, status) {

        }
    });
}
;

function prettyJson(){
    var ugly = document.getElementById('documentWorkflowBody').value;
    var obj = JSON.parse(ugly);
    var pretty = JSON.stringify(obj, undefined, 4);
    document.getElementById('documentWorkflowBody').value = pretty;
};