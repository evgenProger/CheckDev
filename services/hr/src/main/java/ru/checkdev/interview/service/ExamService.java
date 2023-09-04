package ru.checkdev.interview.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class ExamService {

   @JsonIgnoreProperties(ignoreUnknown = true)
   public static class ExamInfo {

      private int id;

      private int result;

      private Calendar start;

      private Calendar finish;

      private Exam exam;

      private String key;

      public String getKey() {
         return key;
      }

      public void setKey(String key) {
         this.key = key;
      }

      public Exam getExam() {
         return exam;
      }

      public void setExam(Exam exam) {
         this.exam = exam;
      }

      public int getId() {
         return id;
      }

      public void setId(int id) {
         this.id = id;
      }

      public int getResult() {
         return result;
      }

      public void setResult(int result) {
         this.result = result;
      }

      public Calendar getStart() {
         return start;
      }

      public void setStart(Calendar start) {
         this.start = start;
      }

      public Calendar getFinish() {
         return finish;
      }

      public void setFinish(Calendar finish) {
         this.finish = finish;
      }
   }

   public static class Exam {
      private int id;

      private String name;

      private String description;
      private boolean active;

      private String intro;

      public int getId() {
         return id;
      }

      public void setId(int id) {
         this.id = id;
      }

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getDescription() {
         return description;
      }

      public void setDescription(String description) {
         this.description = description;
      }

      public boolean isActive() {
         return active;
      }

      public void setActive(boolean active) {
         this.active = active;
      }

      public String getIntro() {
         return intro;
      }

      public void setIntro(String intro) {
         this.intro = intro;
      }
   }

   private final String examUrl;

   @Autowired
   public ExamService(final @Value("${server.exam}") String examUrl) {
      this.examUrl = examUrl;
   }

   public ExamInfo getExamResult(Principal user, String examId) {
      try {
         var node = new ObjectMapper().readValue(
                 new OAuthCall().doGet(
                         user,
                         String.format("%s/answers/result/%s", this.examUrl, examId)
                 ), ObjectNode.class);
         return new ObjectMapper()
                 .readValue(node.toString(), ExamInfo.class);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
}
