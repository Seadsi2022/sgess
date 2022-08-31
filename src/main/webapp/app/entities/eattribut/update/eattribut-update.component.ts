import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EattributFormService, EattributFormGroup } from './eattribut-form.service';
import { IEattribut } from '../eattribut.model';
import { EattributService } from '../service/eattribut.service';
import { IEvaleurattribut } from 'app/entities/evaleurattribut/evaleurattribut.model';
import { EvaleurattributService } from 'app/entities/evaleurattribut/service/evaleurattribut.service';

@Component({
  selector: 'jhi-eattribut-update',
  templateUrl: './eattribut-update.component.html',
})
export class EattributUpdateComponent implements OnInit {
  isSaving = false;
  eattribut: IEattribut | null = null;

  evaleurattributsSharedCollection: IEvaleurattribut[] = [];

  editForm: EattributFormGroup = this.eattributFormService.createEattributFormGroup();

  constructor(
    protected eattributService: EattributService,
    protected eattributFormService: EattributFormService,
    protected evaleurattributService: EvaleurattributService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEvaleurattribut = (o1: IEvaleurattribut | null, o2: IEvaleurattribut | null): boolean =>
    this.evaleurattributService.compareEvaleurattribut(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eattribut }) => {
      this.eattribut = eattribut;
      if (eattribut) {
        this.updateForm(eattribut);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eattribut = this.eattributFormService.getEattribut(this.editForm);
    if (eattribut.id !== null) {
      this.subscribeToSaveResponse(this.eattributService.update(eattribut));
    } else {
      this.subscribeToSaveResponse(this.eattributService.create(eattribut));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEattribut>>): void {
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

  protected updateForm(eattribut: IEattribut): void {
    this.eattribut = eattribut;
    this.eattributFormService.resetForm(this.editForm, eattribut);

    this.evaleurattributsSharedCollection = this.evaleurattributService.addEvaleurattributToCollectionIfMissing<IEvaleurattribut>(
      this.evaleurattributsSharedCollection,
      eattribut.evaleurattribut
    );
  }

  protected loadRelationshipsOptions(): void {
    this.evaleurattributService
      .query()
      .pipe(map((res: HttpResponse<IEvaleurattribut[]>) => res.body ?? []))
      .pipe(
        map((evaleurattributs: IEvaleurattribut[]) =>
          this.evaleurattributService.addEvaleurattributToCollectionIfMissing<IEvaleurattribut>(
            evaleurattributs,
            this.eattribut?.evaleurattribut
          )
        )
      )
      .subscribe((evaleurattributs: IEvaleurattribut[]) => (this.evaleurattributsSharedCollection = evaleurattributs));
  }
}
