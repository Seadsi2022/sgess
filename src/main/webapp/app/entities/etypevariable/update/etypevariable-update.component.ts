import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EtypevariableFormService, EtypevariableFormGroup } from './etypevariable-form.service';
import { IEtypevariable } from '../etypevariable.model';
import { EtypevariableService } from '../service/etypevariable.service';

@Component({
  selector: 'jhi-etypevariable-update',
  templateUrl: './etypevariable-update.component.html',
})
export class EtypevariableUpdateComponent implements OnInit {
  isSaving = false;
  etypevariable: IEtypevariable | null = null;

  editForm: EtypevariableFormGroup = this.etypevariableFormService.createEtypevariableFormGroup();

  constructor(
    protected etypevariableService: EtypevariableService,
    protected etypevariableFormService: EtypevariableFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etypevariable }) => {
      this.etypevariable = etypevariable;
      if (etypevariable) {
        this.updateForm(etypevariable);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etypevariable = this.etypevariableFormService.getEtypevariable(this.editForm);
    if (etypevariable.id !== null) {
      this.subscribeToSaveResponse(this.etypevariableService.update(etypevariable));
    } else {
      this.subscribeToSaveResponse(this.etypevariableService.create(etypevariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtypevariable>>): void {
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

  protected updateForm(etypevariable: IEtypevariable): void {
    this.etypevariable = etypevariable;
    this.etypevariableFormService.resetForm(this.editForm, etypevariable);
  }
}
