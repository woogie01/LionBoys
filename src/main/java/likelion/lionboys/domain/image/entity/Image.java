package likelion.lionboys.domain.image.entity;

import jakarta.persistence.*;
import likelion.lionboys.domain.common.BaseTimeEntity;

@Table(name = "images")
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadStatus uploadStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ContentType contentType;  // DB에 'JPEG', 'PNG' 저장됨
}