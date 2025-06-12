-- ==============================
-- TẠO CSDL
-- ==============================
CREATE DATABASE IF NOT EXISTS quan_ly_khoan_thu;
USE quan_ly_khoan_thu;

-- ==============================
-- BẢNG ho_khau
-- ==============================
CREATE TABLE ho_khau (
                         MaHo           INT AUTO_INCREMENT PRIMARY KEY,
                         SoThanhVien    INT       NOT NULL,
                         DiaChi         VARCHAR(200) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG nhan_khau
-- ==============================
CREATE TABLE nhan_khau (
                           ID             INT AUTO_INCREMENT PRIMARY KEY,
                           CMND           VARCHAR(20),
                           Ten            VARCHAR(50) CHARACTER SET utf8 NOT NULL,
                           Tuoi           INT       NOT NULL,
                           SDT            VARCHAR(15)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG chu_ho
-- ==============================
CREATE TABLE chu_ho (
                        MaHo       INT NOT NULL,
                        IDChuHo    INT NOT NULL,
                        PRIMARY KEY (MaHo, IDChuHo),
                        FOREIGN KEY (MaHo)    REFERENCES ho_khau(MaHo),
                        FOREIGN KEY (IDChuHo) REFERENCES nhan_khau(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG quan_he
-- ==============================
CREATE TABLE quan_he (
                         MaHo         INT    NOT NULL,
                         IDThanhVien  INT    NOT NULL,
                         QuanHe       VARCHAR(30) CHARACTER SET utf8 NOT NULL,
                         PRIMARY KEY (MaHo, IDThanhVien),
                         FOREIGN KEY (MaHo)        REFERENCES ho_khau(MaHo),
                         FOREIGN KEY (IDThanhVien) REFERENCES nhan_khau(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG khoan_thu
-- ==============================
CREATE TABLE khoan_thu (
                           MaKhoanThu    INT AUTO_INCREMENT PRIMARY KEY,
                           TenKhoanThu   VARCHAR(100) NOT NULL,
                           SoTien        DOUBLE,
                           LoaiKhoanThu  ENUM('BatBuoc','DongGop') NOT NULL,
                           CachTinh      ENUM('TheoHo','TheoNguoi','TuNguyen') DEFAULT 'TheoHo',
                           DonViTinh     VARCHAR(50) DEFAULT 'VNĐ',
                           MoTa          TEXT,
                           NgayTao       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG dot_thu
-- ==============================
CREATE TABLE dot_thu (
                         MaDotThu    INT AUTO_INCREMENT PRIMARY KEY,
                         TenDotThu   VARCHAR(100) NOT NULL,
                         MaKhoanThu  INT NOT NULL,
                         NgayBatDau  DATE,
                         NgayKetThuc DATE,
                         FOREIGN KEY (MaKhoanThu) REFERENCES khoan_thu(MaKhoanThu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG nop_tien
-- ==============================
CREATE TABLE `nop_tien` (
                            `IDNopTien`   INT            NOT NULL AUTO_INCREMENT,
                            `IDNhanKhau`  INT            NOT NULL,                             -- FK tới nhan_khau(ID)
                            `MaKhoanThu`  INT            NOT NULL,                             -- FK tới khoan_thu(MaKhoanThu)
                            `MaDotThu`    INT            NOT NULL,                             -- FK tới dot_thu(MaDotThu)
                            `NgayThu`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ngày thực thu
                            `SoTienDaNop` DOUBLE,                                           -- số tiền đã nộp
                            `TrangThai`   ENUM('Da dong','Chua dong','Dong mot phan')
                                                                  DEFAULT 'Da dong',
                            `GhiChu`      TEXT,                                             -- ghi chú (nếu có)
                            `NgayTao`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ngày tạo bản ghi
                            PRIMARY KEY (`IDNopTien`),
                            UNIQUE KEY `uk_noptien` (`IDNhanKhau`,`MaKhoanThu`,`MaDotThu`),
                            CONSTRAINT `fk_noptien_nhankhau`
                                FOREIGN KEY (`IDNhanKhau`) REFERENCES `nhan_khau`(`ID`),
                            CONSTRAINT `fk_noptien_khoanthu`
                                FOREIGN KEY (`MaKhoanThu`) REFERENCES `khoan_thu`(`MaKhoanThu`),
                            CONSTRAINT `fk_noptien_dotthu`
                                FOREIGN KEY (`MaDotThu`)   REFERENCES `dot_thu`(`MaDotThu`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG lich_su_dong_tien
-- ==============================
CREATE TABLE lich_su_dong_tien (
                                   ID            INT AUTO_INCREMENT PRIMARY KEY,
                                   IDNhanKhau    INT    NOT NULL,
                                   MaDotThu      INT    NOT NULL,
                                   SoTienDaDong  DOUBLE NOT NULL,
                                   NgayDong      DATE   NOT NULL,
                                   FOREIGN KEY (IDNhanKhau) REFERENCES nhan_khau(ID),
                                   FOREIGN KEY (MaDotThu)   REFERENCES dot_thu(MaDotThu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG tam_tru
-- ==============================
CREATE TABLE tam_tru (
                         ID            INT AUTO_INCREMENT PRIMARY KEY,
                         IDNhanKhau    INT           NOT NULL,
                         DiaChiTamTru  VARCHAR(200)  NOT NULL,
                         NgayBatDau    DATE          NOT NULL,
                         NgayKetThuc   DATE          DEFAULT NULL,
                         GhiChu        TEXT,
                         CONSTRAINT tam_tru_ibfk_1
                             FOREIGN KEY (IDNhanKhau)
                                 REFERENCES nhan_khau(ID)
                                 ON DELETE CASCADE
                                 ON UPDATE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG vai_tro
-- ==============================
CREATE TABLE vai_tro (
                         IDVaiTro   INT AUTO_INCREMENT PRIMARY KEY,
                         TenVaiTro  VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- ==============================
-- BẢNG users
-- ==============================
CREATE TABLE users (
                       ID         INT AUTO_INCREMENT PRIMARY KEY,
                       username   VARCHAR(30) NOT NULL,
                       passwd     VARCHAR(30) NOT NULL,
                       IDVaiTro   INT,
                       FOREIGN KEY (IDVaiTro) REFERENCES vai_tro(IDVaiTro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

DELIMITER $$

CREATE TRIGGER trg_after_insert_dot_thu
    AFTER INSERT ON dot_thu
    FOR EACH ROW
BEGIN
    DECLARE v_cach_tinh ENUM('TheoHo','TheoNguoi','TuNguyen');

    -- Lấy kiểu tính của khoản thu
    SELECT CachTinh INTO v_cach_tinh
    FROM khoan_thu
    WHERE MaKhoanThu = NEW.MaKhoanThu;

    -- Trường hợp Theo Hộ → Thêm 1 dòng cho mỗi hộ (lấy chủ hộ)
    IF v_cach_tinh = 'TheoHo' THEN
        INSERT INTO nop_tien (IDNhanKhau, MaKhoanThu, MaDotThu, TrangThai)
        SELECT IDChuHo, NEW.MaKhoanThu, NEW.MaDotThu, 'Chua dong'
        FROM chu_ho;

        -- Trường hợp Theo Người → Thêm cho tất cả nhân khẩu
    ELSEIF v_cach_tinh = 'TheoNguoi' THEN
        INSERT INTO nop_tien (IDNhanKhau, MaKhoanThu, MaDotThu, TrangThai)
        SELECT ID, NEW.MaKhoanThu, NEW.MaDotThu, 'Chua dong'
        FROM nhan_khau;

        -- Trường hợp Tự nguyện → Không thêm mặc định
    END IF;

END$$

DELIMITER ;


-- ==============================
-- DỮ LIỆU MẪU
-- ==============================
INSERT INTO ho_khau    (SoThanhVien, DiaChi) VALUES
                                                 (3, 'Khu A - Tầng 1'),
                                                 (2, 'Khu A - Tầng 2');

INSERT INTO nhan_khau  (CMND, Ten, Tuoi, SDT) VALUES
                                                  ('123456789','Nguyễn Văn A',35,'0909123456'),
                                                  ('987654321','Trần Thị B', 32,'0912345678'),
                                                  ('456789123','Nguyễn Văn C',10, NULL);

INSERT INTO chu_ho     (MaHo, IDChuHo) VALUES
                                           (1,1),
                                           (2,2);

INSERT INTO quan_he    (MaHo, IDThanhVien, QuanHe) VALUES
                                                       (1,1,'Chủ hộ'),
                                                       (1,3,'Con'),
                                                       (2,2,'Chủ hộ');

INSERT INTO vai_tro    (TenVaiTro) VALUES
                                       ('Admin'),
                                       ('Ke toan'),
                                       ('To Truong');

INSERT INTO users      (username, passwd, IDVaiTro) VALUES
                                                        ('admin','admin123',1),
                                                        ('ketoan1','ketoan123',2),
                                                        ('totruong','totruong123',3);

INSERT INTO khoan_thu  (TenKhoanThu,SoTien,LoaiKhoanThu,CachTinh,DonViTinh,MoTa) VALUES
                                                                                     ('Phí vệ sinh',6000,'BatBuoc','TheoNguoi','VNĐ/người/tháng','Thu hàng năm'),
                                                                                     ('Ủng hộ 27/07',NULL,'DongGop','TuNguyen','VNĐ','Ủng hộ ngày TBLS');

INSERT INTO dot_thu    (TenDotThu,MaKhoanThu,NgayBatDau,NgayKetThuc) VALUES
                                                                         ('Phí vệ sinh 2025',1,'2025-01-01','2025-12-31'),
                                                                         ('Ủng hộ 27/07/2025',2,'2025-07-01','2025-07-27');

INSERT INTO nop_tien(IDNhanKhau, MaKhoanThu, MaDotThu, NgayThu, SoTienDaNop, TrangThai)VALUES
    (1, 1, 1, '2025-02-01', 216000, 'Da dong'),
    (2, 2, 2, '2025-07-15',  50000, 'Da dong');

INSERT INTO lich_su_dong_tien (IDNhanKhau,MaDotThu,SoTienDaDong,NgayDong) VALUES
                                                                              (1,1,216000,'2025-02-01'),
                                                                              (2,2,50000,'2025-07-15');

INSERT INTO tam_tru    (IDNhanKhau,DiaChiTamTru,NgayBatDau,NgayKetThuc,GhiChu) VALUES
    (3,'Khu B - Tầng 5','2025-03-01',NULL,'Tạm trú đi học');
