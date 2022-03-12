import { Component, OnInit } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Category } from 'src/app/entity/Category.model';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  listOfCategory: Category[] = [];
  userName: string;
  constructor(
    private categoryService: CategoryService,
    private notification: NzNotificationService,
    private modal: NzModalService
  ) {}

  ngOnInit(): void {
    this.getCategories();
    const userName = localStorage.getItem('username') || '';
    if (!(userName === '')) {
      this.userName = userName;
    }
  }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  getCategories() {
    this.categoryService.getAllCategory().subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfCategory = data.results;
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
  logout(){
    this.modal.confirm({
      nzTitle: 'Bạn có muốn đăng xuất!',
      nzContent: '',
      nzOkText: 'Có',
      nzOkType: 'primary',
      nzOkDanger: true,
      nzOnOk: () => {
        localStorage.removeItem('username');
        localStorage.removeItem('auth');
        localStorage.removeItem('userId');
        location.reload();
      },
      nzCancelText: 'No',
      nzOnCancel: () => console.log('Cancel')
    });
   
  }
}
