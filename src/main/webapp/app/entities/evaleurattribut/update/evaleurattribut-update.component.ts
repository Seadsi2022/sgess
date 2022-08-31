import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EvaleurattributFormService, EvaleurattributFormGroup } from './evaleurattribut-form.service';
import { IEvaleurattribut } from '../evaleurattribut.model';
import { EvaleurattributService } from '../service/evaleurattribut.service';

@Component({
  selector: 'jhi-evaleurattribut-update',
  templateUrl: './evaleurattribut-update.component.html',
})
export class EvaleurattributUpdateComponent implements OnInit {
  isSaving = false;
  evaleurattribut: IEvaleurattribut | null = null;

  editForm: EvaleurattributFormGroup = this.evaleurattributFormService.createEvaleurattributFormGroup();

  constructor(
    protected evaleurattributService: EvaleurattributService,
    protected evaleurattributFormService: EvaleurattributFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaleurattribut }) => {
      this.evaleurattribut = evaleurattribut;
      if (evaleurattribut) {
        this.updateForm(evaleurattribut);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaleurattribut = this.evaleurattributFormService.getEvaleurattribut(this.editForm);
    if (evaleurattribut.id !== null) {
      this.subscribeToSaveResponse(this.evaleurattributService.update(evaleurattribut));
    } else {
      this.subscribeToSaveResponse(this.evaleurattributService.create(evaleurattribut));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaleurattribut>>): void {
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

  protected updateForm(evaleurattribut: IEvaleurattribut): void {
    this.evaleurattribut = evaleurattribut;
    this.evaleurattributFormService.resetForm(this.editForm, evaleurattribut);
  }
}
