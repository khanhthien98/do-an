import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  validateForm!: FormGroup;
  userName: String;
  passWord: String;
  isPayment: Boolean = false;
  constructor(
    route: ActivatedRoute,
    private notification: NzNotificationService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) {
    route.params.subscribe((val) => {
      this.isPayment =
        (this.activatedRoute.snapshot.params.isPay || 0) == 1 ? true : false;
    });
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      password: [null, [Validators.required]],
      remember: [true],
    });
  }
  submitForm(): void {
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    this.userName = this.validateForm.controls.userName.value;
    this.passWord = this.validateForm.controls.password.value;
    if (this.userName && this.passWord) {
      this.userService.login(this.userName, this.passWord).subscribe(
        (data) => {
          if (data && data.auth) {
            if (data.auth != 'false') {
              localStorage.setItem('auth', data.auth);
              localStorage.setItem('username', data.username);
              localStorage.setItem('userId', data.userId);
              
              if(this.isPayment){
                this.router.navigate(['/mycart']);
              }else{
                window.location.href = '/home';
              }
            } else {
              this.createNotification(
                'error',
                'Đăng nhập không thành công!',
                'Vui lòng kiểm tra lại tài khoản và mật khẩu.'
              );
            }
          } else {
            this.createNotification(
              'error',
              'Đăng nhập không thành công!',
              'Vui lòng kiểm tra lại tài khoản và mật khẩu.'
            );
          }
        },
        (error) => {
          this.createNotification(
            'error',
            'Đăng nhập không thành công!',
            'Vui lòng kiểm tra lại tài khoản và mật khẩu.'
          );
        }
      );
    }
  }
}
