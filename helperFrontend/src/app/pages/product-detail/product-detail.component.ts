import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Category } from 'src/app/entity/Category.model';
import { Product } from 'src/app/entity/Product.model';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
})
export class ProductDetailComponent implements OnInit {
  productId: String;
  listOfCategory: Category[] = [];
  listOfData: Product[] = [];
  controlArray: Map<string, any> = new Map<string, any>();
  listOfData1: Product[] = [];
  controlArray1: Map<string, any> = new Map<string, any>();v
  product = new Product();
  totalPrice:number = 0;
  cart: Product[] = [];
  constructor(
    route: ActivatedRoute,
    private activatedRoute: ActivatedRoute,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private productService: ProductService,
    private notification: NzNotificationService
  ) {
    route.params.subscribe((val) => {
      this.productId = this.activatedRoute.snapshot.params.id;
    });
  }

  ngOnInit(): void {
    // declare cart 
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
    }

    this.getCategories();
    this.getProducts(1, 4, 'productQuantily', 'ascend');
    this.getDetailProduct(this.productId);
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
  getProductsByCategory(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null,
    category: number
  ) {
    this.controlArray1.set('pageIndex', pageIndex);
    this.controlArray1.set('pageSize', pageSize);
    this.controlArray1.set('sortField', sortField);
    this.controlArray1.set('sortOrder', sortOrder);
    this.controlArray1.set('categoryID', category);
    // get product
    this.productService.getProducts(this.controlArray1).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData1 = data.results;
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
  getProducts(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.productService.getProducts(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData = data.results;
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
  getDetailProduct(id) {
    this.productService.getProduct(id).subscribe(
      (data) => {
        if (data && data.result) {
          this.product = data.result;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      },
      ()=>{
        this.getProductsByCategory(1,6,'productID', 'descend',this.product.category.categoryID);
      }
    );
  }

  addToCart(product:Product){
    let duplicate = false;
    this.cart.forEach((ele) => {
      if (ele.productID == product.productID ) {
        duplicate = true;
      }
    });
    // if no => add item
    if(!duplicate){
      product.quanlityBuy = 1;
      this.cart.push(product);
      this.updateCart();
      this.createNotification(
        'success',
        'Sản phẩm đã thêm vào giỏ hàng',
        ''
      );
    }else{
      this.createNotification(
        'info',
        'Sản phẩm đã có trong giỏ hàng',
        ''
      );
    }
  }
  updateCart() {
    this.totalPrice = 0;
    this.cart.forEach((ele) => {
      this.totalPrice += ele.quanlityBuy * ele.productPrice;
    });
    localStorage.setItem('total', this.totalPrice.toString());
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }
}
