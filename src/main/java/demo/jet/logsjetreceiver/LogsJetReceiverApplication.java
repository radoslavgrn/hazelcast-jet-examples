package demo.jet.logsjetreceiver;

import com.hazelcast.collection.IList;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/logs")
public class LogsJetReceiverApplication {

  static final String FILE_LIST = "FILE-LIST";

  public static void main(String[] args) {
    SpringApplication.run(LogsJetReceiverApplication.class, args);
  }

  @GetMapping("/file")
  public ResponseEntity<ByteArrayResource> getFile() {
    HazelcastInstance hz = Hazelcast.newHazelcastInstance();

    IList<Byte> array = hz.getList(FILE_LIST);

    Byte[] bytes = array.toArray(new Byte[0]);

    ByteArrayResource resource = new ByteArrayResource(ArrayUtils.toPrimitive(bytes));
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .contentLength(resource.contentLength())
        .header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment()
                .filename("test")
                .build().toString())
        .body(resource);
  }

}
