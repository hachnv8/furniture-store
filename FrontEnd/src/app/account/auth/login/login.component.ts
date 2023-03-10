import { Component, OnInit } from "@angular/core";
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";

import { ActivatedRoute, Router } from "@angular/router";
import { finalize } from "rxjs";
import { AuthenticationService } from "src/app/core/services/auth.service";
import { LocalStoreService } from "src/app/core/services/local-storage.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})

/**
 * Login component
 */
export class LoginComponent implements OnInit {
  form: UntypedFormGroup;
  submitted = false;
  error = "";
  returnUrl: string;
  errorResponse = "";

  // set the currenr year
  year: number = new Date().getFullYear();

  // tslint:disable-next-line: max-line-length
  constructor(
    private formBuilder: UntypedFormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthenticationService,
    private localStore: LocalStoreService
  ) {}

  ngOnInit() {
    this.form = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
    });

    // reset login status
    // this.authenticationService.logout();
    // get return url from route parameters or default to '/'
    // tslint:disable-next-line: no-string-literal
    this.returnUrl = this.route.snapshot.queryParams["returnUrl"] || "/";
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.form.controls;
  }

  /**
   * Form submit
   */
  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    this.errorResponse = "";
    this.authService.login(this.form.value).subscribe({
      next: (res) => {
        this.localStore.setUserData(res);
        
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.log(err);
        this.error = err ? err : '';
      },
    });
  }
}
