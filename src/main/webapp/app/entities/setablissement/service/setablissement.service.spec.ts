import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISetablissement } from '../setablissement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../setablissement.test-samples';

import { SetablissementService } from './setablissement.service';

const requireRestSample: ISetablissement = {
  ...sampleWithRequiredData,
};

describe('Setablissement Service', () => {
  let service: SetablissementService;
  let httpMock: HttpTestingController;
  let expectedResult: ISetablissement | ISetablissement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SetablissementService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Setablissement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const setablissement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(setablissement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Setablissement', () => {
      const setablissement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(setablissement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Setablissement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Setablissement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Setablissement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSetablissementToCollectionIfMissing', () => {
      it('should add a Setablissement to an empty array', () => {
        const setablissement: ISetablissement = sampleWithRequiredData;
        expectedResult = service.addSetablissementToCollectionIfMissing([], setablissement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setablissement);
      });

      it('should not add a Setablissement to an array that contains it', () => {
        const setablissement: ISetablissement = sampleWithRequiredData;
        const setablissementCollection: ISetablissement[] = [
          {
            ...setablissement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSetablissementToCollectionIfMissing(setablissementCollection, setablissement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Setablissement to an array that doesn't contain it", () => {
        const setablissement: ISetablissement = sampleWithRequiredData;
        const setablissementCollection: ISetablissement[] = [sampleWithPartialData];
        expectedResult = service.addSetablissementToCollectionIfMissing(setablissementCollection, setablissement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setablissement);
      });

      it('should add only unique Setablissement to an array', () => {
        const setablissementArray: ISetablissement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const setablissementCollection: ISetablissement[] = [sampleWithRequiredData];
        expectedResult = service.addSetablissementToCollectionIfMissing(setablissementCollection, ...setablissementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const setablissement: ISetablissement = sampleWithRequiredData;
        const setablissement2: ISetablissement = sampleWithPartialData;
        expectedResult = service.addSetablissementToCollectionIfMissing([], setablissement, setablissement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setablissement);
        expect(expectedResult).toContain(setablissement2);
      });

      it('should accept null and undefined values', () => {
        const setablissement: ISetablissement = sampleWithRequiredData;
        expectedResult = service.addSetablissementToCollectionIfMissing([], null, setablissement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setablissement);
      });

      it('should return initial array if no Setablissement is added', () => {
        const setablissementCollection: ISetablissement[] = [sampleWithRequiredData];
        expectedResult = service.addSetablissementToCollectionIfMissing(setablissementCollection, undefined, null);
        expect(expectedResult).toEqual(setablissementCollection);
      });
    });

    describe('compareSetablissement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSetablissement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSetablissement(entity1, entity2);
        const compareResult2 = service.compareSetablissement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSetablissement(entity1, entity2);
        const compareResult2 = service.compareSetablissement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSetablissement(entity1, entity2);
        const compareResult2 = service.compareSetablissement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
