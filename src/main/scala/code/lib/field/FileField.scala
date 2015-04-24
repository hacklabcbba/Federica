package code.lib.field

import code.config.MongoConfig
import code.model.FileRecord
import com.mongodb.gridfs.GridFS
import net.liftweb.common.{Box, Full}
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.http.{S, SHtml}
import net.liftweb.json._
import net.liftweb.mongodb.MongoDB
import net.liftweb.mongodb.record.BsonRecord
import net.liftweb.mongodb.record.field.BsonRecordField
import net.liftweb.record.LifecycleCallbacks
import net.liftweb.util.Helpers._
import org.bson.types.ObjectId
import org.joda.time.DateTime

class FileField[OwnerType <: BsonRecord[OwnerType]](rec: OwnerType)
  extends BsonRecordField(rec, FileRecord)
  with LifecycleCallbacks {

  val id = nextFuncName
  val hiddenId = "hidden-" + id
  val hiddenDeleteId = "hiddenDelete-" + id
  private var deletedIds: List[String] = List()

  override def afterSave = {
    println("after Save ejecturado")
    deleteFiles()
  }

  override def toForm = {
    S.appendJs(script)
    Full(
      <input class="fileupload" type="file" name="files" data-url="/upload" id={id} />
      <div id="progress" style="width:20em; border: 1pt solid silver; display: none">
        {SHtml.hidden(s => saveFileIds(s), "", "id" -> hiddenId)}
        {SHtml.hidden(s => setDeletedFileIds(s), "", "id" -> hiddenDeleteId)}
        <div id="progress-bar" style="background: green; height: 1em; width:0%"></div>
      </div>
      <div class="uploadedData" style="display:none;"></div>
    )
  }

  private def saveFileIds(s: String) = {
    implicit lazy val formats: Formats = DefaultFormats
    println("RECORDS:"+s)

    if (s.trim.nonEmpty){

      println("deberia llegar algo:", s)
      for{
        fileId <- tryo((parse(s) \ "fileId").extract[String])
        fileName <- tryo((parse(s) \ "fileName").extract[String])
        fileType <- tryo((parse(s) \ "fileType").extract[String])
        fileSize <- tryo((parse(s) \ "fileSize").extract[Int])
      } yield {
        val res =
          FileRecord
            .createRecord
            .creationDate(DateTime.now().toDate)
            .fileId(fileId)
            .fileName(fileName)
            .fileType(fileType)
            .fileSize(fileSize)
        println("YES:"+res)
        this.set(res)
      }
    }
  }

  private def setDeletedFileIds(s: String) = {
    implicit lazy val formats: Formats = DefaultFormats
    println("RECORDS 2 delete:"+s)
    deletedIds = List() // empty each time

    if (s.trim.nonEmpty) {
      println("deberia llegar lista de datos a borrar...", s)

      /*val separators: Array[Char] = Array('{', '}')
      println("array: "+ s.split(separators) )
      val fileList = s.split(separators)*/

      val fileList = parse(s).extract[List[ItemFiles2Delete]]
      println("extraido con el case class: "+ fileList )

      fileList.map(f => {
        println("seteando para borrar...", f)
        deletedIds = f.fileId :: deletedIds
      })

      println("lista seteada para borrar.." ,deletedIds)
    }
  }

  //callback triggered on afterSave field
  def deleteFiles(): Box[Unit] = tryo {
    MongoDB.use(MongoConfig.defaultId.vend) {
      db =>
        val fs = new GridFS(db)
        deletedIds.map( fId => {
          println("aqui se deberia eliminar el file", fId)
          val id: ObjectId = new ObjectId(fId)
          fs.remove(id)
        })
    }
  }

  private def script = Run{
    """
      $(function () {
          var $uploadInput = $('#""" + id + """');
          var $itemsToSave = $('#""" + hiddenId + """')
          var $itemsToDelete = $('#""" + hiddenDeleteId + """')

          var $containerInfo = $uploadInput.siblings('div.uploadedData');

          $uploadInput.fileupload({
            dataType: 'json',
            add: function (e, data) {
              console.log(data.context);
              $('#progress-bar').css('width', '0%');
              $('#progress').show();
              data.submit();
            },

            progressall: function (e, data) {
              var progress = parseInt(data.loaded / data.total * 100, 10) + '%';
              $('#progress-bar').css('width', progress);
            },

            done: function (e, data) {

              console.log(data.response().result.files);
              $itemsToSave.val(JSON.stringify(data.response().result.files));
              $('#progress').fadeOut();
              $containerInfo.html('');
              var rows = showInfoFiles(data.response().result.files);
              $containerInfo.html(rows);
              $containerInfo.fadeIn();
            }
          });

          function showInfoFiles(files){

            var $rows = $();
            $.each(files, function(i, f){
              var $row = $('<span class="data-item">' +
                  '<a class="link-item" href="#">%fileName</a>' +
                  '<span class="size-item">#</span>' +
                  '[<a href="#" class="remove-item" data-file-id="">x</a>]<br/>' +
                '</span>');

              $row.find(".link-item")
                .attr("href", "files/"+f.fileId)
                .html(f.fileName);

              $row.find(".size-item")
                .html("("+ f.fileSize +")("+ f.fileType +")");

              $row.find(".remove-item")
                .attr("data-file-id", f.fileId)
                .click(function(e){
                    e.preventDefault();
                    var $currentLink2Delete = $(this);
                    var idToDelete = $currentLink2Delete.attr("data-file-id");
                    var lastList = $itemsToDelete.val() || "[]";
                    lastList = JSON.parse(lastList);
                    lastList.push({fileId: idToDelete});
                    $itemsToDelete.val(JSON.stringify(lastList));
                    $itemsToSave.val('');
                    $currentLink2Delete.parents("span.data-item").fadeOut();
                    console.log("remove..", idToDelete);
                });

              $rows = $rows.add($row);

            });
            return $rows;
          }
        });
    """.stripMargin
  }
}

case class ItemFiles2Delete (fileId: String)