package com.active.services.cart.restdocs;

import capital.scalable.restdocs.javadoc.JavadocReaderImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class RestDocument {

    public enum Status {

        Success,

        Error
    }

    public static RestDocumentationResultHandler newSuccessDocument(String moduleName, String apiName, Snippet... snippets) {
        return newDocument(moduleName, apiName, RestDocument.Status.Success, "Success-Example", snippets);
    }

    public static RestDocumentationResultHandler newSuccessDocument(String moduleName, String apiName, String sceneName,
                                                                    Snippet... snippets) {
        return newDocument(moduleName, apiName, RestDocument.Status.Success, sceneName, snippets);
    }

    public static RestDocumentationResultHandler newErrorDocument(String moduleName, String apiName, String sceneName,
                                                                  Snippet... snippets) {
        return newDocument(moduleName, apiName, RestDocument.Status.Error, sceneName, snippets);
    }

    public static RestDocumentationResultHandler newDocument(String moduleName, String apiName, RestDocument.Status status,
                                                             String sceneName, Snippet... snippets) {
        List<Snippet> snippetList = Stream.of(snippets).collect(Collectors.toList());
        snippetList.add(resource(apiName + "-" + status.toString()));
        return document(moduleName + "/" + apiName + "/" + status.toString() + "/" + sceneName,
          preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), snippetList.toArray(new Snippet[0]));
    }

    public static ApiDescriptionSnippet autoApiDescriptionDoc(Class controllerClazz, String controllerMethodName) {
        String description = JavadocReaderImpl.createWithSystemProperty()
          .resolveMethodComment(controllerClazz, controllerMethodName);
        return new ApiDescriptionSnippet(description);
    }

    public static RequestFieldsSnippet autoRequestFieldsDoc(Object object) {
        return requestFields(generateAllFieldDescriptors(object));
    }

    public static ResponseFieldsSnippet autoResponseFieldsDoc(Object object) {
        List<FieldDescriptor> fieldDescriptors = generateAllFieldDescriptors(object);
        return responseFields(generateAllFieldDescriptors(object));
    }

    public static ParameterDescriptor autoPathParameterDoc(String parameterName, Class clazz, String fieldName)
      throws NoSuchFieldException {
        Field field = null;
        Class currentClass = clazz;
        while (currentClass != null) {
            try {
                field = currentClass.getDeclaredField(fieldName);
            } catch (Exception e) {}
            if (Objects.nonNull(field)) {
                return parameterWithName(parameterName)
                        .attributes(new Attributes.Attribute("type", field.getType().getSimpleName()),
                                new Attributes.Attribute("constraint", getPathParameterConstraint(field)))
                        .description(getComment(currentClass, fieldName));
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new NoSuchFieldException(fieldName);
    }

    public static SimpleSnippet autoSimpleDoc(String snippetName, String templateName) {
        return new SimpleSnippet(snippetName, templateName);
    }

    private static List<FieldDescriptor> generateAllFieldDescriptors(Object object) {
        if (!(object instanceof Collection)) {
            return generateAllFieldDescriptors("", object);
        } else if (!CollectionUtils.isEmpty((Collection) object)) {
            Object elementObject = ((Collection) object).iterator().next();
            return generateAllFieldDescriptors("[]", elementObject);
        }
        return new ArrayList<>();
    }

    private static List<FieldDescriptor> generateAllFieldDescriptors(String path, Object object) {
        final String pathPrefix = StringUtils.isEmpty(path) ? path : (path + ".");
        List<FieldDescriptor> fieldDescriptors = generateAllFieldDescriptors(pathPrefix, object.getClass());
        fieldDescriptors.addAll(Stream.of(object.getClass().getDeclaredFields())
          .filter(field -> field.isAnnotationPresent(Valid.class))
          .map(field -> {
              Object fieldObject = ReflectionTestUtils.getField(object, object.getClass(), field.getName());
              if (!(fieldObject instanceof Collection)) {
                  return generateAllFieldDescriptors(pathPrefix + field.getName(), fieldObject);
              } else if (!CollectionUtils.isEmpty((Collection) fieldObject)) {
                  Object elementObject = ((Collection) fieldObject).iterator().next();
                  return generateAllFieldDescriptors(pathPrefix + field.getName() + "[]", elementObject);
              }
              return new ArrayList<FieldDescriptor>();
          })
          .flatMap(Collection::stream)
          .collect(Collectors.toList()));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> generateAllFieldDescriptors(String path, Class clazz) {
        List<List<FieldDescriptor>> fieldDescriptorGroups = new ArrayList<>();
        Class currentClass = clazz;
        while (currentClass != null) {
            fieldDescriptorGroups.add(generateFieldDescriptors(path, currentClass));
            currentClass = currentClass.getSuperclass();
        }
        Collections.reverse(fieldDescriptorGroups);
        return fieldDescriptorGroups.stream()
          .flatMap(Collection::stream)
          .collect(Collectors.toList());
    }

    private static List<FieldDescriptor> generateFieldDescriptors(String path, Class clazz) {
        return Stream.of(clazz.getDeclaredFields())
          .filter(field -> !"$jacocoData".equals(field.getName()))
          .map(field -> fieldWithPath(path + field.getName())
            .type(getType(field))
            .description(getComment(clazz, field.getName()))
            .attributes(new Attributes.Attribute("constraint", getConstraint(field))))
          .collect(Collectors.toList());
    }

    private static String getType(Field field) {
        if (field.getType().getName().startsWith("java")) {
            return field.getType().getSimpleName();
        }
        if (field.getType().isEnum()) {
            return  "Enum[" + Stream.of(field.getType().getEnumConstants())
              .map(Object::toString)
              .collect(Collectors.joining(", "))
              + "]";
        }
        return "Object";
    }

    private static String getComment(Class clazz, String fieldName) {
        return JavadocReaderImpl.createWithSystemProperty()
          .resolveFieldComment(clazz, fieldName);
    }

    private static String getConstraint(Field field) {
        List<String> constraints = Stream.of(field.getAnnotations())
          .filter(annotation -> "javax.validation.constraints".equals(annotation.annotationType().getPackage().getName()))
          .map(annotation -> {
              String annotationName = annotation.annotationType().getSimpleName();
              if ("Size".equals(annotationName)) {
                  Size size = (Size) annotation;
                  annotationName += "[" + size.min() + "-" + size.max() + "]";
              }
              else if ("Min".equals(annotationName)) {
                  Min min = (Min) annotation;
                  annotationName += "[" + min.value() + "]";
              }
              else if ("Digits".equals(annotationName)) {
                  Digits digits = (Digits) annotation;
                  annotationName += "[integer=" + digits.integer() + ",fraction=" + digits.fraction() + "]";
              }
              return annotationName;
          })
          .collect(Collectors.toList());
        Optional.ofNullable(field.getAnnotation(JsonFormat.class))
          .ifPresent(jsonFormat -> constraints.add("pattern[" + jsonFormat.pattern() + "]"));
        return constraints.stream()
          .collect(Collectors.joining(", "));
    }

    private static String getPathParameterConstraint(Field field) {
        String constraint = getConstraint(field);
        if ("String".equals(field.getType().getSimpleName()) && !constraint.contains("NotBlank")) {
            constraint = "NotBlank" + (StringUtils.isEmpty(constraint) ? "" : ", ") + constraint;
        }
        if (!constraint.contains("NotBlank") && !constraint.contains("NotNull")) {
            constraint = "NotNull" + (StringUtils.isEmpty(constraint) ? "" : ", ") + constraint;
        }
        return constraint;
    }
}
