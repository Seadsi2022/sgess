import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EattributvariableFormService, EattributvariableFormGroup } from './eattributvariable-form.service';
import { IEattributvariable } from '../eattributvariable.model';
import { EattributvariableService } from '../service/eattributvariable.service';
import { IEvariable } from 'app/entities/evariable/evariable.model';
import { EvariableService } from 'app/entities/evariable/service/evariable.service';

@Component({
  selector: 'jhi-eattributvariable-update',
  templateUrl: './eattributvariable-update.component.html',
})
export class EattributvariableUpdateComponent implements OnInit {
  isSaving = false;
  eattributvariable: IEattributvariable | null = null;

  evariablesSharedCollection: IEvariable[] = [];

  editForm: EattributvariableFormGroup = this.eattributvariableFormService.createEattributvariableFormGroup();

  constructor(
    protected eattributvariableService: EattributvariableService,
    protected eattributvariableFormService: EattributvariableFormService,
    protected evariableService: EvariableService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEvariable = (o1: IEvariable | null, o2: IEvariable | null): boolean => this.evariableService.compareEvariable(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eattributvariable }) => {
      this.eattributvariable = eattributvariable;
      if (eattributvariable) {
        this.updateForm(eattributvariable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eattributvariable = this.eattributvariableFormService.getEattributvariable(this.editForm);
    if (eattributvariable.id !== null) {
      this.subscribeToSaveResponse(this.eattributvariableService.update(eattributvariable));
    } else {
      this.subscribeToSaveResponse(this.eattributvariableService.create(eattributvariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEattributvariable>>): void {
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

  protected updateForm(eattributvariable: IEattributvariable): void {
    this.eattributvariable = eattributvariable;
    this.eattributvariableFormService.resetForm(this.editForm, eattributvariable);

    this.evariablesSharedCollection = this.evariableService.addEvariableToCollectionIfMissing<IEvariable>(
      this.evariablesSharedCollection,
      eattributvariable.evariable
    );
  }

  protected loadRelationshipsOptions(): void {
    this.evariableService
      .query()
      .pipe(map((res: HttpResponse<IEvariable[]>) => res.body ?? []))
      .pipe(
        map((evariables: IEvariable[]) =>
          this.evariableService.addEvariableToCollectionIfMissing<IEvariable>(evariables, this.eattributvariable?.evariable)
        )
      )
      .subscribe((evariables: IEvariable[]) => (this.evariablesSharedCollection = evariables));
  }
}
