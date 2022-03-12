package com.example.helper.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

	// new
	public static final String GIUPVIEC_HVT = "GIUPVIEC_HVT";
	public static final Long LOGIN_FAIL = 1L;
	public static final String PROCESS = "Process";
	public static final String DELIVERY = "Delivery";
	public static final String DELIVERED = "Delivered";
	public static final String CANCEL = "Cancel";
	public static final Long LOGIN_SUCCESS = 0L;
	public static final String ROOT_IMAGES_BACKEND = "F:/HaUI/Do an/Chuong_trinh/helperBackEnd/src/assets/images";
	public static final String ROOT_IMAGES_FRONTEND = "F:/HaUI/Do an/Chuong_trinh/helperBackEnd/src/assets/images";

	public enum ChucNang {
		TT_QD_PCUQ(0, "Thông tin quy định PCUQ"), PCUQ(1, "Khai báo PCUQ tại ban KDV&TT"),
		KSV_LD_PHE_DUYET_GD(2, "KSV/Lãnh đạo phê duyệt GD"),
		HD_KHUNG_HD_DAM_BAO(3, "Thông tin hợp đồng khung và hợp đông đảm bảo"), HAN_MUC_BO(4, "Hạn mức BO"),
		NHOM_EMAIL(5, "Nhóm email"), QUY_CHUAN_CT(6, "Quy chuẩn chứng từ"), DM_TSDB_BIDV(7, "Danh mục TSĐB tại BIDV"),
		DM_TSDB_BIDV_CC_TC(8, "Danh mục TSĐB BIDV cầm cố, thế cập tại đối tác"), CHUNG_TU_GD(9, "Chứng từ giao dịch"),
		THONG_TIN_GD(10, "Thông tin giao dịch"), THEO_DOI_CHUYEN_TRA(11, "Theo dõi chuyển trả"),
		THE0_DOI_GD_TSDB(12, "Theo dõi giao dịch TSĐB"), THEO_DOI_GD_VUOT_HM(13, "Theo dõi giao dịch vượt hạn mức"),
		KB_DOI_TAC_CAN_THEO_DOI(14, "Khai báo đối tác cần theo dõi"),
		THEO_DOI_HOP_DONG_GOC(15, "Theo dõi hợp đồng gốc"), BAO_CAO_TG_XU_LY_GD(16, "Báo cáo thời gian xử lý GD"),
		VAN_TIN(17, "Vấn tin"), BAO_CAO_HANG_NGAY(18, "Báo cáo hàng ngày"),
		DS_GD_CHUA_DU_CT(19, "Danh sách giao dịch chưa đủ CT"),
		KHAI_BAO_DOI_TAC_CTD(20, "Khai Báo Đối Tác Cần Theo Dõi"),
		CAP_NHAT_DU_LIEU_TINH(21, "Cập nhật thông tin dữ liệu tĩnh");

		private int funcode;
		private String funcname;

		private ChucNang(int funcode, String funcname) {
			this.funcode = funcode;
			this.funcname = funcname;
		}

		public int getFuncode() {
			return funcode;
		}

		public void setFuncode(int funcode) {
			this.funcode = funcode;
		}

		public String getFuncname() {
			return funcname;
		}

		public void setFuncname(String funcname) {
			this.funcname = funcname;
		}
	}
}
