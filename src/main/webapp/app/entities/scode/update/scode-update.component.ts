import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ScodeFormService, ScodeFormGroup } from './scode-form.service';
import { IScode } from '../scode.model';
import { ScodeService } from '../service/scode.service';

@Component({
  selector: 'jhi-scode-update',
  templateUrl: './scode-update.component.html',
})
export class ScodeUpdateComponent implements OnInit {
  isSaving = false;
  scode: IScode | null = null;

  editForm: ScodeFormGroup = this.scodeFormService.createScodeFormGroup();

  constructor(
    protected scodeService: ScodeService,
    protected scodeFormService: ScodeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scode }) => {
      this.scode = scode;
      if (scode) {
        this.updateForm(scode);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const scode = this.scodeFormService.getScode(this.editForm);
    if (scode.id !== null) {
      this.subscribeToSaveResponse(this.scodeService.update(scode));
    } else {
      this.subscribeToSaveResponse(this.scodeService.create(scode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScode>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(scode: IScode): void {
    this.scode = scode;
    this.scodeFormService.resetForm(this.editForm, scode);
  }
}
