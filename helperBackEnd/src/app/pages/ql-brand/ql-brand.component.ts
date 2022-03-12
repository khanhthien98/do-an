import { Component, OnInit } from '@angular/core';
import { BrandService } from 'src/app/services/brand.service';
import { Brand } from '../../entity/Brand.model';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
import { NzModalService } from 'ng-zorro-antd/modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
@Component({
  selector: 'app-ql-brand',
  templateUrl: './ql-brand.component.html',
  styleUrls: ['./ql-brand.component.css'],
})
export class QlBrandComponent implements OnInit {
  constructor(
    private brandService: BrandService,
    private notification: NzNotificationService,
    private modal: NzModalService,
    private fb: FormBuilder
  ) {}
  isVisible = false;
  total = 1;
  loading = true;
  pageSize = 5;
  pageIndex = 1;
  listOfData: Brand[] = [];
  brand = new Brand();
  isEdit = false;
  validateForm!: FormGroup;
  controlArray: Map<string, any> = new  Map<string, any>();

  ngOnInit(): void {
    // this.getBrands(this.pageIndex, this.pageSize, null, null);
    this.validateForm = this.fb.group({
      brandId: [null],
      brandName: [null, [Validators.required]],
    });
  }
  showModal(id): void {
    this.isVisible = true;
    if (id) {
      this.isEdit = true;
      this.listOfData.forEach((item) => {
        if (item.brandID == id) {
          this.validateForm.controls.brandId.setValue(item.brandID);
          this.validateForm.controls.brandName.setValue(item.brandName);
        }
      });
    } else {
      this.validateForm.controls.brandId.setValue(null);
      this.validateForm.controls.brandName.setValue('');
    }
  }

  handleOk(): void {
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    const brandId = this.validateForm.controls.brandId.value;
    const brandName = this.validateForm.controls.brandName.value;
    if (brandName && brandId) {
      this.brand.brandID = brandId;
      this.brand.brandName = brandName;

      this.brandService.saveBrand(this.brand).subscribe(
        (data) => {
          this.createNotification('success', 'Sửa thành công!', '');
        },
        (error) => {
          this.createNotification(
            'error',
            'Có lỗi xảy ra!',
            'Vui lòng liên hệ quản trị viên.'
          );
        },
        () => {
          this.isVisible = false;
          this.getBrands(this.pageIndex, this.pageSize, null, null);
        }
      );
    } else if (brandName) {
      this.brand.brandID = null;
      this.brand.brandName = brandName;

      this.brandService.saveBrand(this.brand).subscribe(
        (data) => {
          this.createNotification('success', 'Thêm thành công!', '');
        },
        (error) => {
          this.createNotification(
            'error',
            'Có lỗi xảy ra!',
            'Vui lòng liên hệ quản trị viên.'
          );
        },
        () => {
          this.isVisible = false;
          this.getBrands(this.pageIndex, this.pageSize, null, null);
        }
      );
    }
  }

  handleCancel(): void {
    this.isVisible = false;
  }
  onQueryParamsChange(params: NzTableQueryParams): void {
    const { pageSize, pageIndex, sort } = params;
    const currentSort = sort.find((item) => item.value !== null);
    const sortField = (currentSort && currentSort.key) || null;
    const sortOrder = (currentSort && currentSort.value) || null;
    this.getBrands(pageIndex, pageSize, sortField, sortOrder);
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  getBrands(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    // get brands
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    this.brandService.getBrands(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.loading = false;
          this.listOfData = data.results;
          this.total = data.rowCount;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }

  delBrand(id) {
    this.modal.confirm({
      nzTitle: 'Bạn có chắc chắn muốn xóa?',
      nzContent: '',
      nzOkText: 'Yes',
      nzOkType: 'primary',
      nzOkDanger: true,
      nzOnOk: () =>
        this.brandService.deleteBrand(id).subscribe(
          (data) => {
            this.createNotification('success', 'Xoá thành công!', '');
          },
          (error) => {
            this.createNotification(
              'error',
              'Có lỗi xảy ra!',
              'Vui lòng liên hệ quản trị viên.'
            );
          },
          () => {
            this.getBrands(this.pageIndex, this.pageSize, null, null);
          }
        ),
      nzCancelText: 'No',
      nzOnCancel: () => console.log('Cancel'),
    });
  }
}
