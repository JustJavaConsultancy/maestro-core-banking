package ng.com.justjava.corebanking.web.rest;

import java.io.File;
import java.io.IOException;

import ng.com.justjava.corebanking.service.dto.EmailNotificationDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailNotificationResource {

	private final Logger log = LoggerFactory.getLogger(EmailNotificationResource.class);

	Utility utility;

	@Value("${app.document-url}")
	private String documentUrl;

	public EmailNotificationResource(Utility utility) {
		this.utility = utility;
	}

	/**
	 * {@code POST  /emailnotification} : Send an Email notification.
	 *
	 */
	@PostMapping("/contact-form")
	public ResponseEntity<EmailNotificationDTO> sendNotification(@RequestBody EmailNotificationDTO notificationDTO) {
		log.debug("REST request for sending EmailNotification : {}", notificationDTO);
		return new ResponseEntity<>(utility.sendEmailWithAttachment(notificationDTO, documentUrl), HttpStatus.OK);
	}

	@GetMapping("/contact-form/support/{uuid}")
	public ResponseEntity<byte[]> getAttachment(@PathVariable String uuid) {
		byte[] image = new byte[0];

		String filename = "customer-complaint-attachment-" + uuid + ".jpg";
		String filename2 = "customer-complaint-attachment-" + uuid + ".pdf";
		String filename5 = "customer-complaint-attachment-" + uuid + ".png";

		String pathname = documentUrl + filename;
		String pathname2 = documentUrl + filename2;
		String pathname5 = documentUrl + filename5;

		try {
			log.debug("MediaType.IMAGE_JPEG");
			File file = new File(pathname);
			image = FileUtils.readFileToByteArray(file);
			log.debug("Image Pathname : " + pathname);
			log.debug("Image Byte : " + documentUrl);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

		} catch (IOException e) {
			e.printStackTrace();

			try {
				File file = new File(pathname2);
				image = FileUtils.readFileToByteArray(file);
				log.debug("Image Pathname : " + pathname2);
				log.debug("Image Byte : " + documentUrl);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);

			} catch (Exception xe) {
				xe.getSuppressed();
				try {
					File file = new File(pathname5);
					image = FileUtils.readFileToByteArray(file);
					log.debug("Image Pathname : " + pathname5);
					log.debug("Image Byte : " + documentUrl);
					return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);

				} catch (Exception ev) {
					ev.getSuppressed();

				}
			}
		}

		return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);

	}

}
