package code.rest

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.UUID
import javax.imageio.ImageIO

import code.config.MongoConfig
import code.model.FileRecord
import code.model.resource.Room
import com.foursquare.rogue.LiftRogue._
import com.mongodb.gridfs.{GridFS, GridFSDBFile}
import net.liftmodules.imaging.ImageResizer
import net.liftweb.common._
import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{FileParamHolder, JsonResponse, LiftResponse, _}
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import net.liftweb.mongodb.MongoDB
import net.liftweb.util.Helpers._
import org.joda.time.DateTime

object AjaxFileUpload extends RestHelper {

  serve {

    case "upload" :: Nil Post req =>
      val jValue = "files" -> req.uploadedFiles.map(file => {
        saveFile(file)
      })
      response(jValue)

    case "upload" :: "image" :: Nil Post req =>
      for {
        file <- req.uploadedFiles.headOption
      } yield responseImage(saveImage(file))



    case "files" :: fileName:: dFileName :: Nil Get req =>
      serveFile(fileName, req, {
        (fileName) =>
          Room.where(_.plane.subfield(_.fileId) eqs fileName).select(_.plane).fetch(1).headOption match{
            case Some(fi) =>
              fi.fileName.get
            case _ =>
              dFileName
          }
      })

    case "image" :: fileName :: Nil Get req =>
      serveSingleImage(fileName, req)

    case "file" :: "preview" :: fileName :: Nil Get req =>
      servePreviewFile(fileName, req)
  }


  private def response(jvalue: JValue): LiftResponse = {
    JsonResponse(jvalue, 200)
  }

  private def responseImage(jvalue: JValue): LiftResponse = {
    JsonResponse(jvalue, 200)
  }

  private def saveFile(fph: FileParamHolder) = {
    val inst: FileRecord = FileRecord.createRecord
    val auxName = fph.fileName
    val auxLength = fph.length
    val auxType = fph.mimeType

    //Get current date using Joda Time
    val today = DateTime.now()
    val fileCreationDate = today.toDate

    val fileMongoName = org.apache.commons.codec.digest.DigestUtils.md5Hex(fph.fileStream) + UUID.randomUUID()
    val file = inst.fileId(fileMongoName).fileName(auxName).
      fileType(auxType).fileSize(auxLength).creationDate(fileCreationDate)

    //Write file in Gridfs
    val f = writeFile(fph, fileMongoName)

    ("fileId" -> fileMongoName) ~
    ("fileName" -> auxName) ~
    ("fileType" -> auxType) ~
    ("fileSize" -> auxLength)
  }

  private def saveImage(fph: FileParamHolder) = {
    val inst: FileRecord = FileRecord.createRecord
    val auxName = fph.fileName
    val auxLength = fph.length
    val auxType = fph.mimeType

    //Get current date using Joda Time
    val today = DateTime.now()
    val fileCreationDate = today.toDate

    val fileMongoName = org.apache.commons.codec.digest.DigestUtils.md5Hex(fph.fileStream) + UUID.randomUUID()
    val file = inst.fileId(fileMongoName).fileName(auxName).
      fileType(auxType).fileSize(auxLength).creationDate(fileCreationDate)

    //Write file in Gridfs
    val f = writeFile(fph, fileMongoName)

    ("fileId" -> fileMongoName) ~
    ("fileName" -> auxName) ~
    ("fileType" -> auxType) ~
    ("url" -> s"/image/$fileMongoName") ~
    ("uploaded" -> 1) ~
    ("fileSize" -> auxLength)
  }

  private def writeFile(fp: FileParamHolder, fileMongoName: String) = {
    MongoDB.use(MongoConfig.defaultId.vend) {
      db =>
        val fs = new GridFS(db)
        val mongoFile = fs.createFile(fp.fileStream)
        mongoFile.setFilename(fileMongoName)
        mongoFile.setContentType(fp.mimeType)
        mongoFile.save()
    }
  }

  private def serveFile(fileName:String, req: Req, dFileName: String => String): Box[LiftResponse] = {

    MongoDB.use(MongoConfig.defaultId.vend){
      db =>
        val fs = new GridFS(db)
        fs.findOne(fileName) match {

          case file: GridFSDBFile =>
            val lastModified = file.getUploadDate.getTime
            val downloadFileName = dFileName(fileName)

              Full(req.testFor304(lastModified, "Expires" -> toInternetDate(millis + 10.days)) openOr {
              val headers =
                ("Content-Disposition" -> ("attachment; filename="+ downloadFileName ) ) ::
                ("Content-Type" -> file.getContentType) ::
                ("Pragma" -> "") ::
                ("Cache-Control" -> "") ::
                ("Last-Modified" -> toInternetDate(lastModified)) ::
                ("Expires" -> toInternetDate(millis + 10.days)) ::
                ("Date" -> nowAsInternetDate) :: Nil

              val stream = file.getInputStream
              println("stream: ", stream)

              StreamingResponse(
                stream,
                () => stream.close,
                file.getLength,
                headers, Nil, 200)
            })
          case _ =>
            println("file record empty")
            Empty
        }
      }
    }

  private def serveSingleImage(fileName:String, req: Req): Box[LiftResponse] = {

    MongoDB.use(MongoConfig.defaultId.vend){

      db =>
        val fs = new GridFS(db)
        fs.findOne(fileName) match {

          case file: GridFSDBFile =>
            val lastModified = file.getUploadDate.getTime
            Full(req.testFor304(lastModified, "Expires" -> toInternetDate(millis + 10.days)) openOr {
              val headers =
                ("Content-Type" -> file.getContentType) ::
                  ("Pragma" -> "") ::
                  ("Cache-Control" -> "") ::
                  ("Last-Modified" -> toInternetDate(lastModified)) ::
                  ("Expires" -> toInternetDate(millis + 10.days)) ::
                  ("Date" -> nowAsInternetDate) :: Nil

              val img = ImageIO.read(file.getInputStream)
              val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
              ImageIO.write(img, file.getContentType.split("/")(1), baos)
              val b = baos.toByteArray
              baos.close()
              val stream = new ByteArrayInputStream(b)

              StreamingResponse(
                stream,
                () => stream.close,
                b.length,
                headers, Nil, 200
              )
            })

          case _ =>
            Empty
        }
    }
  }

  private def servePreviewFile(fileName:String, req: Req): Box[LiftResponse] = {

    MongoDB.use(MongoConfig.defaultId.vend){

      db =>
        val fs = new GridFS(db)
        fs.findOne(fileName) match {

          case file: GridFSDBFile =>
            val lastModified = file.getUploadDate.getTime
            Full(req.testFor304(lastModified, "Expires" -> toInternetDate(millis + 10.days)) openOr {
              val headers =
                ("Content-Type" -> file.getContentType) ::
                  ("Pragma" -> "") ::
                  ("Cache-Control" -> "") ::
                  ("Last-Modified" -> toInternetDate(lastModified)) ::
                  ("Expires" -> toInternetDate(millis + 10.days)) ::
                  ("Date" -> nowAsInternetDate) :: Nil

              val img = ImageIO.read(file.getInputStream)
              val im: BufferedImage = ImageResizer.max(Empty, img, 75, 75)
              val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
              ImageIO.write(im, file.getContentType.split("/")(1), baos)
              val b = baos.toByteArray
              baos.close()
              val stream = new ByteArrayInputStream(b)

              StreamingResponse(
                stream,
                () => stream.close,
                b.length,
                headers, Nil, 200
              )
            })

          case _ =>
            Empty
        }
    }
  }

  private def withFileToDo(fileName:String, success: GridFSDBFile => String, fail: String => String ): String = {

    var result = ""
    MongoDB.use(MongoConfig.defaultId.vend){
      db =>
        val fs = new GridFS(db)
        result = fs.findOne(fileName) match {
          case f: GridFSDBFile =>
            success(f)
          case _ =>
            fail(fileName)
        }
    }
    result
  }

}