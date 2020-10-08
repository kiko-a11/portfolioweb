var deleteMsg='削除してよろしいですか？';
var deleteMsgErr='削除に失敗しました。';

$(function(){
    function projectRenumber() {
        $("#main-body-container > .row-body.project").each(function(r){
            var regName = new RegExp('^(projectList\\[)[0-9]+(\\]\\..*)$', 'g');
            var name = "";

            var regId = new RegExp('^(projectList)[0-9]+(\\..*)$', 'g');
            var id = "";

            var regModal = new RegExp('^(#?linkModal|#?fileModal|link-modal-body-container|file-modal-body-container)[0-9]+$', 'g');
            var targetModal = "";

            $(this).find("label.sort-renumber:not(.row-body.link *):not(.row-body.file *)").each(function(){
                $(this).html('#' + (r + 1));
            });

            $(this).find("input.sort-renumber:not(.row-body.link *):not(.row-body.file *)").each(function(){
                name = $(this).attr('name').replace(regName, "$1" + r + "$2");
                $(this).attr('name', name);
                id = $(this).attr('id').replace(regId, "$1" + r + "$2");
                $(this).attr('id', id);
                $(this).val(r);
            });

            $(this).find("button.button-renumber").each(function(){
                targetModal = $(this).attr('data-target').replace(regModal, "$1" + r);
                $(this).attr('data-target', targetModal);
            });

            $(this).find("div.modal.button-renumber").each(function(){
                targetModal = $(this).attr('id').replace(regModal, "$1" + r);
                $(this).attr('id', targetModal);
            });

            $(this).find("div.container-fluid.button-renumber").each(function(){
                targetModal = $(this).attr('id').replace(regModal, "$1" + r);
                $(this).attr('id', targetModal);
            });

            $(this).find(".renumber:not(.row-body.link *):not(.row-body.file *)").each(function(){
                name = $(this).attr('name').replace(regName, "$1" + r + "$2");
                $(this).attr('name', name);
                id = $(this).attr('id').replace(regId, "$1" + r + "$2");
                $(this).attr('id', id);
            });
        });

        $("input[name^=projectList]input[name$=\\.sortNo]:not(.row-body.link *):not(.row-body.file *)").each(function(){
            linkRenumber($(this).val());
            fileRenumber($(this).val());
        });

    };

    function linkRenumber(buttonNo) {
        $("#link-modal-body-container" + buttonNo + " .row-body.link").each(function(r){
            var regName = new RegExp('^(projectList\\[)[0-9]+(\\]\\.linkList\\[)[0-9]+(\\]\\..*)$', 'g');
            var name = "";

            var regId = new RegExp('^(projectList)[0-9]+(\\.linkList)[0-9]+(\\..*)$', 'g');
            var id = "";

            $(this).find("label.sort-renumber").each(function(){
                $(this).html('#' + (r + 1));
            });

            $(this).find("input.sort-renumber").each(function(){
                name = $(this).attr('name').replace(regName, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('name', name);
                id = $(this).attr('id').replace(regId, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('id', id);
                $(this).val(r);
            });

            $(this).find(".renumber").each(function(){
                name = $(this).attr('name').replace(regName, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('name', name);
                id = $(this).attr('id').replace(regId, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('id', id);
            });
        });
    };

    function fileRenumber(buttonNo) {
        $("#file-modal-body-container" + buttonNo + " .row-body.file").each(function(r){
            var regName = new RegExp('^(projectList\\[)[0-9]+(\\]\\.fileLinkList\\[)[0-9]+(\\]\\..*)$', 'g');
            var name = "";

            var regId = new RegExp('^(projectList)[0-9]+(\\.fileLinkList)[0-9]+(\\..*)$', 'g');
            var id = "";

            $(this).find("label.sort-renumber").each(function(){
                $(this).html('#' + (r + 1));
            });

            $(this).find("input.sort-renumber").each(function(){
                name = $(this).attr('name').replace(regName, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('name', name);
                id = $(this).attr('id').replace(regId, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('id', id);
                $(this).val(r);
            });

            $(this).find(".renumber").each(function(){
                name = $(this).attr('name').replace(regName, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('name', name);
                id = $(this).attr('id').replace(regId, "$1" + buttonNo + "$2" + r + "$3");
                $(this).attr('id', id);
            });
        });
    };

    function projectDelete(){
        var deleteConfirm = confirm(deleteMsg);
        if(deleteConfirm == true) {
            var row = $(this).closest(".row-body.project");
            var id = row.find("input[name$=\\.id]:first").val();
            console.log(id);

            if (id == "-1") {
                row.remove();
                projectRenumber();
            }
            else {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) {
                  xhr.setRequestHeader(header, token);
                });

                var url = $("meta[name='_context_path']").attr("content") + '/home/deleteproject';

                $.ajax({
                    url: url,
                    type: 'POST',
                    contentType: "application/json",
                    dataType : 'json',
                    data: JSON.stringify({
                        "id": id,
                    })
                }).done(function() {
                    row.remove();
                    projectRenumber();

                }).fail(function() {
                    alert(deleteMsgErr);
                });
            };
        }
        else {
            (function(e) {
              e.preventDefault()
            });
        };
    }

    function linkDelete(){
        var deleteConfirm = confirm(deleteMsg);
        if(deleteConfirm == true) {
            var buttonNo = $(this).parents(".row-body.project").find("input[name^=projectList]input[name$=\\.sortNo]:first").val();
            var row = $(this).closest(".row-body.link");
            var id = row.find("input[name$=\\.id]:first").val();

            if (id == "-1") {
                row.remove();
                linkRenumber(buttonNo);
            }
            else {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) {
                  xhr.setRequestHeader(header, token);
                });

                var url = $("meta[name='_context_path']").attr("content") + '/home/deletelink';

                $.ajax({
                    url: url,
                    type: 'POST',
                    contentType: "application/json",
                    dataType : 'json',
                    data: JSON.stringify({
                        "id": id,
                    })
                }).done(function() {
                    row.remove();
                    linkRenumber(buttonNo);
                }).fail(function() {
                    alert(deleteMsgErr);
                });
            };
        }
        else {
            (function(e) {
              e.preventDefault()
            });
        };
    }

    function fileDelete(){
        var deleteConfirm = confirm(deleteMsg);
        if(deleteConfirm == true) {
            var buttonNo = $(this).parents(".row-body.project").find("input[name^=projectList]input[name$=\\.sortNo]:first").val();
            var row = $(this).closest(".row-body.file");
            var id = row.find("input[name$=\\.id]:first").val();

            if (id == "-1") {
                row.remove();
                fileRenumber(buttonNo);
            }
            else {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) {
                  xhr.setRequestHeader(header, token);
                });

                var url = $("meta[name='_context_path']").attr("content") + '/home/deletefile';

                $.ajax({
                    url: url,
                    type: 'POST',
                    contentType: "application/json",
                    dataType : 'json',
                    data: JSON.stringify({
                        "id": id,
                    })
                }).done(function() {
                    row.remove();
                    fileRenumber(buttonNo);
                }).fail(function() {
                    alert(deleteMsgErr);
                });
            };
        }
        else {
            (function(e) {
              e.preventDefault()
            });
        };
    }
    function projectAdd(){
        var userId = $("header > input#userId").val();

        var rowForm = '' +
        '<div class="row-body project">' +
        '    <input type="hidden" class="renumber" id="projectList999.id" name="projectList[999].id" value="-1">' +
        '    <input type="hidden" class="renumber" id="projectList999.userId" name="projectList[999].userId" value="' + userId + '">' +
        '    <div class="container-pw container-bg-pw">' +
        '        <div class="form-group">' +
        '            <div class="row">' +
        '                <div class="col-lg-1">' +
        '                    <label class="sort-renumber d-inline d-lg-none head-light-pw"></label>' +
        '                    <label class="float-right sort-renumber d-none d-lg-inline head-light-pw"></label>' +
        '                    <input type="hidden" class="sort-renumber" id="projectList999.sortNo" name="projectList[999].sortNo">' +
        '                </div>' +
        '                <div class="col-lg-1">' +
        '                    <label class="control-label head-light-pw">期間</label>' +
        '                </div>' +
        '                <div class="col-lg-5">' +
        '                    <div class="input-group">' +
        '                        <input type="date" class="form-control renumber" id="projectList999.startDate" name="projectList[999].startDate">' +
        '                        <div>～</div>' +
        '                        <input type="date" class="form-control renumber" id="projectList999.endDate" name="projectList[999].endDate">' +
        '                    </div>' +
        '                    ' +
        '                    ' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="form-group">' +
        '            <div class="row">' +
        '                <div class="col-lg-1 offset-lg-1">' +
        '                    <label class="control-label head-light-pw">経歴</label>' +
        '                </div>' +
        '                <div class="col-lg-10">' +
        '                    <input type="text" class="form-control renumber inner-row" placeholder="経歴の名称" id="projectList999.name" name="projectList[999].name">' +
        '                    ' +
        '                    <textarea class="form-control renumber inner-row" rows="5" placeholder="概要" id="projectList999.overview" name="projectList[999].overview"></textarea>' +
        '                </div>' +
        '                <div class="col-lg-10 offset-lg-2">' +
        '                    <div class="text-right-pw">' +
        '                        <button type="button" name="linkButton" class="btn btn-primary button-renumber inner-row" data-toggle="modal" data-target="#linkModal999">リンク編集</button>' +
        '                        <button type="button" name="fileButton" class="btn btn-primary button-renumber inner-row" data-toggle="modal" data-target="#fileModal999">添付ファイル編集</button>' +
        '                    </div>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="form-group">' +
        '            <div class="row">' +
        '                <div class="col-lg-1 offset-lg-1">' +
        '                    <label class="control-label head-light-pw">メモ</label>' +
        '                </div>' +
        '                <div class="col-lg-10">' +
        '                    <textarea class="form-control renumber" rows="5" placeholder="メモ" id="projectList999.memo" name="projectList[999].memo"></textarea>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-lg-12 text-right-pw">' +
        '                <button type="button" class="btn btn-secondary rounded-circle p-0 rounded-button project-delete">－</button>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '    <div class="modal fade button-renumber" id="linkModal999" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
        '        <div class="modal-dialog modal-xl" role="document">' +
        '            <div class="modal-content">' +
        '                <div class="modal-header">' +
        '                    <h5 class="modal-title">リンク編集</h5>' +
        '                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
        '                        <span aria-hidden="true">×</span>' +
        '                    </button>' +
        '                </div>' +
        '                <div class="modal-body">' +
        '                    <div class="container-fluid button-renumber" id="link-modal-body-container999">' +
        '                        <div class="row row-head link">' +
        '                            <div class="col-lg-3 offset-lg-1 d-none d-lg-block">' +
        '                                <label class="head-light-pw">リンク</label>' +
        '                            </div>' +
        '                            <div class="col-lg-2 d-none d-lg-block">' +
        '                                <label class="head-light-pw">画面表示名</label>' +
        '                            </div>' +
        '                            <div class="col-lg-6 d-none d-lg-block">' +
        '                                <label class="head-light-pw">サムネイル指定</label>' +
        '                            </div>' +
        '                        </div>' +
        '                    </div>' +
        '                    <button type="button" class="btn btn-secondary rounded-circle p-0 rounded-button link-add margin-top margin-bottom">＋</button>' +
        '                    <div class="modal-footer">' +
        '                        <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>' +
        '                    </div>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '    <div class="modal fade button-renumber" id="fileModal999" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
        '        <div class="modal-dialog modal-xl" role="document">' +
        '            <div class="modal-content">' +
        '                <div class="modal-header">' +
        '                    <h5 class="modal-title">添付ファイル編集</h5>' +
        '                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
        '                        <span aria-hidden="true">×</span>' +
        '                    </button>' +
        '                </div>' +
        '                <div class="modal-body">' +
        '                    <div class="container-fluid button-renumber" id="file-modal-body-container999">' +
        '                        <div class="row row-head file">' +
        '                            <div class="col-lg-2 offset-lg-1 d-none d-lg-block">' +
        '                                <label class="head-light-pw">画面表示名</label>' +
        '                            </div>' +
        '                            <div class="col-lg-3 d-none d-lg-block">' +
        '                                <label class="head-light-pw">添付ファイル</label>' +
        '                            </div>' +
        '                            <div class="col-lg-6 d-none d-lg-block">' +
        '                                <label class="head-light-pw">サムネイル指定</label>' +
        '                            </div>' +
        '                        </div>' +
        '                    </div>' +
        '                    <button type="button" class="btn btn-secondary rounded-circle p-0 rounded-button file-add margin-top margin-bottom">＋</button>' +
        '                    <div class="modal-footer">' +
        '                        <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>' +
        '                    </div>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>';

        $(rowForm).appendTo($('#main-body-container'));

        setEvents();

        projectRenumber();
        checkChange();
    }

    function linkAdd(){
        var buttonNo = $(this).parents(".row-body.project").find("input[name^=projectList]input[name$=\\.sortNo]:first").val();
        var projectId = $(this).parents(".row-body.project").find("input[name^=projectList]input[name$=\\.id]:first").val();

        var rowForm = '' +
        '<div class="row row-body link border-row-pw">' +
        '    <input type="hidden" class="renumber" id="projectList999.linkList999.id" name="projectList[999].linkList[999].id" value="-1">' +
        '    <input type="hidden" class="renumber" id="projectList999.linkList999.projectId" name="projectList[999].linkList[999].projectId" value="' + projectId + '">' +
        '    <div class="col-lg-12 margin-top">' +
        '        <label class="sort-renumber d-inline d-lg-none head-light-pw">#1</label>' +
        '    </div>' +
        '    <div class="col-lg-1">' +
        '        <label class="float-right sort-renumber head-light-pw d-none d-lg-inline">#1</label>' +
        '        <input type="hidden" class="sort-renumber" id="projectList999.linkList999.sortNo" name="projectList[999].linkList[999].sortNo">' +
        '    </div>' +
        '    <div class="col-lg-12 d-inline d-lg-none margin-top">' +
        '        <label class="head-light-pw">リンク</label>' +
        '    </div>' +
        '    <div class="col-lg-3">' +
        '        <input type="text" class="form-control renumber" placeholder="http://xxxxxx.xxx" id="projectList999.linkList999.url" name="projectList[999].linkList[999].url">' +
        '        ' +
        '    </div>' +
        '    <div class="col-lg-12 d-inline d-lg-none margin-top">' +
        '        <label class="head-light-pw">画面表示名</label>' +
        '    </div>' +
        '    <div class="col-lg-2">' +
        '        <input type="text" class="form-control renumber" placeholder="画面表示名" id="projectList999.linkList999.displayName" name="projectList[999].linkList[999].displayName">' +
        '        ' +
        '    </div>' +
        '    <div class="col-lg-12 d-inline d-lg-none margin-top">' +
        '        <label class="head-light-pw">サムネイル指定</label>' +
        '    </div>' +
        '    <div class="col-lg-5">' +
        '        <div class="row">' +
        '            <div class="col-lg-4">' +
        '                <label><input type="checkbox" class="fileUpload renumber" id="projectList999.linkList999.thumbnailFlag" name="projectList[999].linkList[999].thumbnailFlag">指定する</label>' +
        '            </div>' +
        '            <div class="col-lg-8 form-group">' +
        '                <label class="fileUpload disabled"><input type="file" class="renumber" accept="image/x-png,image/gif,image/jpeg" id="projectList999.linkList999.upload" name="projectList[999].linkList[999].upload" disabled="">ファイル選択</label>' +
        '                <input type="text" readonly="" class="form-control renumber readonlyText" id="projectList999.linkList999.thumbnailFileName" name="projectList[999].linkList[999].thumbnailFileName" placeholder="">' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '    <div class="col-lg-1">' +
        '        <button type="button" class="btn btn-secondary rounded-circle p-0 rounded-button link-delete margin-bottom">－</button>' +
        '    </div>' +
        '</div>';

        $(rowForm).appendTo($('#link-modal-body-container' + buttonNo));

        setEvents();

        linkRenumber(buttonNo);
        checkChange();
    }

    function fileAdd(){
        var buttonNo = $(this).parents(".row-body.project").find("input[name^=projectList]input[name$=\\.sortNo]:first").val();
        var projectId = $(this).parents(".row-body.project").find("input[name^=projectList]input[name$=\\.id]:first").val();

        var rowForm = '' +
        '<div class="row row-body file border-row-pw">' +
        '    <input type="hidden" class="renumber" id="projectList999.fileLinkList999.id" name="projectList[999].fileLinkList[999].id" value="-1">' +
        '    <input type="hidden" class="renumber" id="projectList999.fileLinkList999.projectId" name="projectList[999].fileLinkList[999].projectId" value="' + projectId + '">' +
        '    <div class="col-lg-12 margin-top">' +
        '        <label class="sort-renumber d-inline d-lg-none head-light-pw">#1</label>' +
        '    </div>' +
        '    <div class="col-lg-1">' +
        '        <label class="float-right sort-renumber head-light-pw d-none d-lg-inline">#1</label>' +
        '        <input type="hidden" class="sort-renumber" id="projectList999.fileLinkList999.sortNo" name="projectList[999].fileLinkList[999].sortNo">' +
        '    </div>' +
        '    <div class="col-lg-12 d-inline d-lg-none margin-top">' +
        '        <label class="head-light-pw">画面表示名</label>' +
        '    </div>' +
        '    <div class="col-lg-2">' +
        '        <input type="text" class="form-control renumber" placeholder="画面表示名" id="projectList999.fileLinkList999.displayName" name="projectList[999].fileLinkList[999].displayName">' +
        '        ' +
        '    </div>' +
        '    <div class="col-lg-12 d-inline d-lg-none margin-top">' +
        '        <label class="head-light-pw">添付ファイル</label>' +
        '    </div>' +
        '    <div class="col-lg-3 form-group">' +
        '        <label class="fileUpload"><input type="file" class="renumber" id="projectList999.fileLinkList999.dataFileUpload" name="projectList[999].fileLinkList[999].dataFileUpload">ファイル選択</label>' +
        '        <input type="text" readonly="" class="form-control renumber readonlyText" id="projectList999.fileLinkList999.dataFileName" name="projectList[999].fileLinkList[999].dataFileName">' +
        '        ' +
        '    </div>' +
        '    <div class="col-lg-12 d-inline d-lg-none margin-top">' +
        '        <label class="head-light-pw">サムネイル指定</label>' +
        '    </div>' +
        '    <div class="col-lg-5">' +
        '        <div class="row">' +
        '            <div class="col-lg-4">' +
        '                <label><input type="checkbox" class="fileUpload renumber" id="projectList999.fileLinkList999.thumbnailFlag" name="projectList[999].fileLinkList[999].thumbnailFlag">指定する</label>' +
        '            </div>' +
        '            <div class="col-lg-8 form-group">' +
        '                <label class="fileUpload"><input type="file" class="renumber" accept="image/x-png,image/gif,image/jpeg" id="projectList999.fileLinkList999.thumbnailFileUpload" name="projectList[999].fileLinkList[999].thumbnailFileUpload">ファイル選択</label>' +
        '                <input type="text" readonly="" class="form-control renumber readonlyText" id="projectList999.fileLinkList999.thumbnailFileName" name="projectList[999].fileLinkList[999].thumbnailFileName" placeholder="png/gif/jpeg">' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '    <div class="col-lg-1">' +
        '        <button type="button" class="btn btn-secondary rounded-circle p-0 rounded-button file-delete margin-bottom">－</button>' +
        '    </div>' +
        '</div>';

        $(rowForm).appendTo($('#file-modal-body-container' + buttonNo));

        setEvents();

        fileRenumber(buttonNo);
        checkChange();
    }

    function fileUpload(){
        var fileNameTag = $(this).closest('label.fileUpload').next('input[readonly]');
        var fileName = $(this).val().replace(/.*[\/\\]/, '');

        fileNameTag.val(fileName);
//        console.log("a" + $(this).val());
    }

    function fileCheckBoxChange(){
        var col = $(this).closest('div[class|=col]').next('div[class|=col]');
        var thumbnailFileLabelTag= col.children('label.fileUpload');
        var thumbnailFileTag     = col.find('input[type=file]');
        var thumbnailFileNameTag = col.find('input[type=text]');

        if ($(this).prop("checked") == true) {
            thumbnailFileLabelTag.removeClass('disabled');
            thumbnailFileTag.prop('disabled', false);
            thumbnailFileNameTag.attr('placeholder', "png/gif/jpeg");
        }
        else {
            thumbnailFileLabelTag.addClass('disabled');
            thumbnailFileTag.prop('disabled', true);
            thumbnailFileTag.val(null);
            thumbnailFileNameTag.val(null);
            thumbnailFileNameTag.attr('placeholder', "");
        }
    }

    function checkChange(){
        var token = "";
        var l = 20;
        var c = "abcdefghijklmnopqrstuvwxyz0123456789";
        var cl = c.length;
        for(var i=0; i<l; i++){
          token += c[Math.floor(Math.random()*cl)];
        }

        $('#token').val(token);
//        console.log($('#token').val());
    }

    function buttonErrCheck(){
//        var errButton = $('div.modal[id^=linkModal],div.modal[id^=fileModal]');
        var errButton = $('button[name=linkButton],button[name=fileButton]');
        errButton.each(function(){
            var errModalId = $(this).attr('data-target');
            var errTag = $(errModalId).find('.err');
//            console.log('errTag:' + errTag.html());

            if(errTag.length){
                $(this).addClass('btn-danger');
                $(this).removeClass('btn-primary');

                var errButtonName = $(this).attr('name');
                if (errButtonName=='linkButton') {
                    $(this).parent().append('<div class="err">リンクの入力内容にエラーがあります</div>');
                }
                else if (errButtonName=='fileButton') {
                    $(this).parent().append('<div class="err">添付ファイルの入力内容にエラーがあります</div>');
                }
            }
        });
    }

    function setEvents(){
        $('button.project-add').off('click');
        $('button.project-add').on('click', projectAdd);

        $('button.project-delete').off('click');
        $('button.project-delete').on('click', projectDelete);

        $('button.link-add').off('click');
        $('button.link-add').on('click', linkAdd);

        $('button.link-delete').off('click');
        $('button.link-delete').on('click', linkDelete);

        $('button.file-add').off('click');
        $('button.file-add').on('click', fileAdd);

        $('button.file-delete').off('click');
        $('button.file-delete').on('click', fileDelete);

//        $('input').off('change');
//        $('input').on('change', checkChange);
//        $('textarea').off('change');
//        $('textarea').on('change', checkChange);

        $('label.fileUpload input[type=file]').on('change', fileUpload);
        var fl = $('input.fileUpload[type=checkbox]');
        fl.on('change', fileCheckBoxChange);
        fl.each(fileCheckBoxChange);

    }

    $('#header-home').addClass('active');

    setEvents();
    buttonErrCheck();

    projectRenumber();
    if ($("#main-body-container > .row-body.project").length == 0) {
        projectAdd();
    }

    checkChange();

});